package com.example.viewmodel

import android.app.Application
import android.content.ContentResolver
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.example.model.bean.Image
import com.example.model.bean.ImageFolder
import com.example.model.bean.PrivateImage
import com.example.model.database.DatabaseManager
import com.example.viewmodel.base.BaseViewModel
import com.example.viewmodel.utils.FileUtils
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.lang.Exception
import java.util.HashSet

class MainViewModel: BaseViewModel() {
    var result = MutableLiveData<String>()

    var imageFolders = ObservableArrayList<ImageFolder>()
    var images = ObservableArrayList<Image>()
    var tempFolders = ArrayList<ImageFolder>()
    var tempImages = ArrayList<Image>()
    var allImages = ArrayList<Image>()

    var initialled = MutableLiveData<Boolean>()

    var currentAlbum = MutableLiveData<ImageFolder>()
    var totalAlbumId = 0

    var selectedImages = ArrayList<Image>()
    var encryptImages = ObservableArrayList<PrivateImage>()

    var selectNumber = MutableLiveData<Int>()

    var selectInfo = MutableLiveData<String>()

    var encryptResult = MutableLiveData<Boolean>()

/*    constructor(mApplication: Application):super(mApplication) {
        //getImageList()
    }*/

    fun initial(application: Application): MainViewModel {
        mApplication = application
        //updateEncryptImage()
        getImageList()
        return this
    }

    private fun getImageList() {
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            //Toast.makeText(context, "当前存储卡不可用", Toast.LENGTH_SHORT).show()
            result.value = "当前存储卡不可用"
            return
        }

        mDisPosable.add(Flowable.just(1).flatMap {
            try {
                scanImages()
            } catch (e: Exception) {
                throw Throwable(e.message)
            }

            return@flatMap Flowable.just(1)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            currentAlbum.value = tempFolders[0]
            images.addAll(allImages)
            imageFolders.addAll(tempFolders)
            initialled.value = true
        }, {
            result.value = ""
            Log.e("MainViewModel","getImageList failed! Error: " + it.message)
        }))
    }

    private fun updateEncryptImage() {
        Flowable.just(1).flatMap {
            var list = DatabaseManager.dbManager.getPrivateImageDao().getAll()
            return@flatMap Flowable.just(list)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            encryptImages.clear()
            for (privateImage in it) {
                encryptImages.add(privateImage)
            }
            encryptResult.value = true
        },{
            Log.e("MainViewModel","encrypt insert")
        })
    }

    fun encrypt() {
        if (selectedImages.isEmpty()) {
            encryptResult.value = true
            return
        }
        Flowable.just(selectedImages).flatMap {
            var encryptList = ArrayList<PrivateImage>()
            for (image in it) {
                var privateFilename = FileUtils.getImagePrivateFileName()
                var privateImagePath = FileUtils.getPrivateDirectory() + privateFilename
                var privateImage = PrivateImage(0, privateFilename, image.filename, image.path, false)
                if(FileUtils.moveImage(image.path , privateImagePath, mApplication!!)) {
                    encryptList.add(privateImage)
                } else {
                    Log.i("MainViewModel", "move image failure!")
                }
            }
            DatabaseManager.dbManager.getPrivateImageDao().insert(encryptList)
            return@flatMap Flowable.just(1)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            updateEncryptImage()
        },{
            Log.e("MainViewModel","encrypt insert")
        })


    }

    fun checkImage(image: Image, isChecked: Boolean) {
        try {
            if (isChecked) {
                selectedImages.add(image)
            } else {
                for (img in selectedImages) {
                    if (img.path == image.path) {
                        selectedImages.remove(img)
                        break
                    }
                }
            }

            selectNumber.value = selectedImages.size

            if (selectedImages.size == 0) {
                selectInfo.value = "退出"
            } else {
                selectInfo.value = "确定(" + selectedImages.size + ")"
            }
        } catch (e:Exception) {
            Log.e("MainViewModel", "checkImage Error:" + e.message)
        }

    }

    fun selectAlbum(album: ImageFolder) {
        if (album.id == currentAlbum.value!!.id) return
        if (totalAlbumId == album.id) {
            currentAlbum.value = album
            images.clear()
            images.addAll(allImages)
            return
        }
        mDisPosable.add(Flowable.just(album).flatMap {
            tempImages.clear()
            //val parentFile = File(it.firstImagePath).parentFile
            val parentFile = File(it.dir)
            if (parentFile != null) {
                parentFile.list{dir: File, name: String ->
                    if (name.endsWith(".jpg") ||
                        name.endsWith(".jpeg") ||
                        name.endsWith(".png")) {
                        tempImages.add(Image(name, dir.path + "/" + name, false))
                        return@list true
                    }
                    return@list false
                }
            } else {
                throw NullAlbumDirectoryException("Album " + it.dirName + " Directory is null")
            }

            return@flatMap Flowable.just(true)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            currentAlbum.value = album
            images.clear()
            images.addAll(tempImages)
        }, {
            result.value = ""
            Log.e("MainViewModel","selectAlbum failed! Error: " + it.message)
        }))

    }

    private fun scanImages() {
        val muri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cr: ContentResolver = mApplication!!.contentResolver
        val cursor = cr.query(
            muri,
            null,
            MediaStore.Images.Media.MIME_TYPE + " = ? or " + MediaStore.Images.Media.MIME_TYPE + " = ? ",
            arrayOf("image/jpeg", "image/png"),
            MediaStore.Images.Media.DATE_MODIFIED
        )
        val dirPaths: MutableSet<String> =
            HashSet()
        val count = cursor!!.count
        for (i in count - 1 downTo 0) {
            cursor.moveToPosition(i)
            val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            if (i == count - 1) {
                val imageFolder = ImageFolder(i, "",path,"所有图片",0,true,0)
                totalAlbumId = i
                tempFolders.add(imageFolder)
            }
            val imgBean = Image(File(path).name, path, false)
            allImages.add(imgBean)
            val parentFile = File(path).parentFile ?: continue
            val dirPath = parentFile.absolutePath
            var imageFolder: ImageFolder? = null
            if (dirPaths.contains(dirPath)) {
                continue
            } else {
                dirPaths.add(dirPath)
                imageFolder = ImageFolder(i, dirPath, path, parentFile.name, 0, false, 1)
                tempFolders.add(imageFolder)
            }
            if (parentFile.list() == null) continue
            val picSize = parentFile.list { dir, s ->
                s.endsWith(".jpg") || s.endsWith(".jpeg") || s.endsWith(".png")
            }.size
            imageFolder.size = picSize
        }
        cursor.close()
        /*val message = Message.obtain()
        message.what = 0x001
        handler.sendMessage(message)*/
    }

    inner class NullAlbumDirectoryException(message: String): Exception(message)

    companion object {
        private var instance = MainViewModel()
        fun getInstance() = instance
    }
}