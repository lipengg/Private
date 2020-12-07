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
    var loadImageListResult = MutableLiveData<Boolean>()

    var imageFolders = ObservableArrayList<ImageFolder>()
    var images = ObservableArrayList<Image>()
    var tempFolders = ArrayList<ImageFolder>()
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
        selectInfo.value = "退出"
        updateEncryptImage()
        //getImageList()
        return this
    }

    fun loadImageList() {
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            //Toast.makeText(context, "当前存储卡不可用", Toast.LENGTH_SHORT).show()
            result.value = "当前存储卡不可用"
            return
        }

        mDisPosable.add(Flowable.just(1).flatMap {
            try {
                allImages.clear()
                tempFolders.clear()
                scanImages()
            } catch (e: Exception) {
                throw Throwable(e.message)
            }

            return@flatMap Flowable.just(1)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            images.clear()
            imageFolders.clear()

            currentAlbum.value = tempFolders[0]
            images.addAll(allImages)
            imageFolders.addAll(tempFolders)
            result.value = ""
            loadImageListResult.value = true
        }, {
            result.value = "相册加载失败!"
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
            initialled.value = true
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
            it.clear()
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
        currentAlbum.value!!.selected = false
        album.selected = true
        if (totalAlbumId == album.id) {
            currentAlbum.value = album
            images.clear()
            images.addAll(allImages)
            return
        }
        var tempImages = ArrayList<Image>()
        mDisPosable.add(Flowable.just(album).flatMap { it ->
            mApplication!!.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Images.Media.DATA + " like ?",
                arrayOf(it.dir + "%"),
                MediaStore.Images.Media.DATE_MODIFIED
            )?.let{cursor->
                val count = cursor!!.count
                for (i in count - 1 downTo 0) {
                    cursor.moveToPosition(i)
                    val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                    tempImages.add(Image(File(path).name, path, false))
                }
                cursor.close()
            }
            return@flatMap Flowable.just(true)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            currentAlbum.value = album
            images.clear()
            images.addAll(tempImages)
        }, {
            result.value = "打开图库失败!"
            Log.e("MainViewModel","selectAlbum failed! Error: " + it.message)
        }))

    }

    private fun scanImages() {
        val muri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cr: ContentResolver = mApplication!!.contentResolver
        val cursor = cr.query(
            muri,
            null,
            null,
            null,
            MediaStore.Images.Media.DATE_MODIFIED
        )
        var albumId = 0
        val dirPaths: MutableSet<String> = HashSet()
        val count = cursor!!.count
        for (i in count - 1 downTo 0) {
            cursor.moveToPosition(i)
            val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            if (i == count - 1) {
                val imageFolder = ImageFolder(albumId, "",path,"所有图片", count,true,0)
                totalAlbumId = imageFolder.id
                tempFolders.add(imageFolder)
                albumId += 1
            }
            val imgBean = Image(File(path).name, path, false)
            allImages.add(imgBean)
            val parentFile = File(path).parentFile ?: continue
            val dirPath = parentFile.absolutePath
            if (parentFile.name =="0" || dirPaths.contains(dirPath)) {
                continue
            } else {
                dirPaths.add(dirPath)
                var imageFolder: ImageFolder = ImageFolder(albumId, dirPath, path, parentFile.name, 0, false, 1)
                tempFolders.add(imageFolder)
                albumId += 1

                cr.query(
                    muri,
                    null,
                    MediaStore.Images.Media.DATA + " like ?",
                    arrayOf("$dirPath%"),
                    MediaStore.Images.Media.DATE_MODIFIED
                )?.count?.let{
                    imageFolder.size = it
                }
            }
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