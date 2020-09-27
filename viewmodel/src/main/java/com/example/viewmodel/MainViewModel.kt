package com.example.viewmodel

import android.app.Application
import android.content.ContentResolver
import android.os.Environment
import android.provider.MediaStore
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.example.model.bean.Image
import com.example.model.bean.ImageFolder
import com.example.viewmodel.base.BaseViewModel
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

    var current_gallery = ObservableArrayList<String>()

/*    constructor(mApplication: Application):super(mApplication) {
        //getImageList()
    }*/

    fun initial(application: Application): MainViewModel {
        mApplication = application
        getImageList()
        return this
    }

    fun getImageList() {
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
            images.addAll(allImages)
            /*for (image in allImages) {
                images.add(image)
            }*/
            imageFolders.addAll(tempFolders)
        }, {
            result.value = ""
        }))
    }

    private fun selectGallery(gallery: String?) {

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
                val imageFolder = ImageFolder("",path,"所有图片",0,true,0)
                tempFolders.add(imageFolder)
            }
            val imgBean = Image(path, false)
            //images.add(imgBean)
            allImages.add(imgBean)
            val parentFile = File(path).parentFile ?: continue
            val dirPath = parentFile.absolutePath
            var imageFolder: ImageFolder? = null
            if (dirPaths.contains(dirPath)) {
                continue
            } else {
                dirPaths.add(dirPath)
                imageFolder = ImageFolder(dirPath, path, parentFile.name, 0, false, 1)
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

    companion object {
        private var instance = MainViewModel()
        fun getInstance() = instance
    }
}