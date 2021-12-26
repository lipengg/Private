package com.example.viewmodel

import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.model.bean.*
import com.example.model.database.DatabaseManager
import com.example.viewmodel.base.BaseViewModel
import com.example.viewmodel.utils.FileUtils
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.lang.Exception
import java.util.HashSet

typealias MyFile = com.example.model.bean.File

class MainViewModel: BaseViewModel() {
    // ***********************常量***********************
    val imageFlag = PrivateFileType.IMAGE.value
    val videoFlag = PrivateFileType.VIDEO.value

    private val viewModel = 0
    private val editModel = 1

    var pageFlag = MutableLiveData<Int>()

    var result = MutableLiveData<String>()

    var tempFolders = ArrayList<Folder>()



    var initialled = MutableLiveData<Boolean>()

    private var totalFolderId = 0

    var selectedFiles = ArrayList<MyFile>()
    var encryptFiles = ObservableArrayList<PrivateFile>()

    var selectNumber = MutableLiveData<Int>()

    var selectInfo = MutableLiveData<String>()

    var encryptResult = MutableLiveData<Boolean>()

    var folders = ObservableArrayList<Folder>()
    var files = ObservableArrayList<MyFile>()
    var allFiles = ArrayList<MyFile>()

    var currentFolder = MutableLiveData<Folder>()
    var loadFileListResult = MutableLiveData<Boolean>()

    var pageModel = MutableLiveData<Int>()

/*    constructor(mApplication: Application):super(mApplication) {
        //getImageList()
    }*/

    fun initial(application: Application): MainViewModel {
        mApplication = application
        pageModel.value = viewModel
        pageFlag.value = imageFlag
        selectInfo.value = "退出"
        updateEncryptFile(true)
        return this
    }

    fun switchModel() {
        if(pageModel.value == viewModel) pageModel.value = editModel else pageModel.value = viewModel
    }

    fun reloadPageInfo() {
        Log.e("MainViewModel", pageFlag.value?.toString())
        pageFlag.value?.let {
            encryptFiles.clear()
            updateEncryptFile()
        }
    }

    fun loadFileList() {
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            //Toast.makeText(context, "当前存储卡不可用", Toast.LENGTH_SHORT).show()
            result.value = "当前存储卡不可用"
            return
        }

        mDisPosable.add(Flowable.just(1).flatMap {
            try {
                allFiles.clear()
                tempFolders.clear()
                scanFiles()
            } catch (e: Exception) {
                throw Throwable(e.message)
            }

            return@flatMap Flowable.just(1)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            files.clear()
            folders.clear()

            currentFolder.value = tempFolders[0]
            files.addAll(allFiles)
            folders.addAll(tempFolders)
            result.value = ""
            loadFileListResult.value = true
        }, {
            result.value = "文件加载失败!"
            Log.e("MainViewModel","loadFileList failed! Error: " + it.message)
        }))
    }

    private fun getPageFlag(): Int {
        var flag = imageFlag
        pageFlag.value?.let{
            flag = it
        }
        return flag;
    }

    private fun getCurrentFileType(): Int {
        return getPageFlag()
    }

    private fun getCurrentFileStatus(): Int {
        return PrivateFileStatus.Valid.value
    }

    private fun getCurrentPageFiles() :List<PrivateFile> {
        val type = getPageFlag()
        val status = PrivateFileStatus.Valid.value
        return DatabaseManager.dbManager.getPrivateFileDao().getFiles(type, status)
    }

    private fun updateEncryptFile(isInit: Boolean = false) {
        Flowable.just(1).flatMap {
            var list = getCurrentPageFiles()
            return@flatMap Flowable.just(list)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            encryptFiles.clear()
            for (privateFile in it) {
                encryptFiles.add(privateFile)
            }
            encryptResult.value = true
            if(isInit)
                initialled.value = true
        },{
            Log.e("MainViewModel","encrypt insert")
        })
    }

    fun resetSelectFile() {
        selectedFiles.clear()
        selectNumber.value = 0
        selectInfo.value = "退出"
    }

    fun clearSelectPrivateFile() {

    }

    fun encrypt() {
        if (selectedFiles.isEmpty()) {
            encryptResult.value = true
            return
        }
        Flowable.just(selectedFiles).flatMap {
            var encryptList = ArrayList<PrivateFile>()
            for (myFile in it) {
                var privateFilename = FileUtils.getImagePrivateFileName()
                var privateFilePath = FileUtils.getPrivateDirectory() + privateFilename
                var privateFile = PrivateFile(0, privateFilename, getCurrentFileType(), getCurrentFileStatus(), myFile.name, myFile.path, false)
                if(FileUtils.moveImage(myFile.path , privateFilePath, mApplication!!)) {
                    encryptList.add(privateFile)
                } else {
                    Log.i("MainViewModel", "move file failure!")
                }
            }
            DatabaseManager.dbManager.getPrivateFileDao().insert(encryptList)
            return@flatMap Flowable.just(1)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            updateEncryptFile()
            resetSelectFile()
        },{
            Log.e("MainViewModel","encrypt insert")
        })


    }

    fun checkPrivateFile(file: PrivateFile, isChecked: Boolean) {

    }

    fun checkFile(file: MyFile, isChecked: Boolean) {
        try {
            file.selected = isChecked
            if (isChecked) {
                selectedFiles.add(file)
            } else {
                for (sFile in selectedFiles) {
                    if (sFile.path == file.path) {
                        selectedFiles.remove(sFile)
                        break
                    }
                }
            }

            if (currentFolder.value!!.id != totalFolderId) {
                for (myFile in allFiles) {
                    if (myFile.path == file.path) {
                        myFile.selected = isChecked
                    }
                }
            }

            selectNumber.value = selectedFiles.size

            if (selectedFiles.size == 0) {
                selectInfo.value = "退出"
            } else {
                selectInfo.value = "确定(" + selectedFiles.size + ")"
            }
        } catch (e:Exception) {
            Log.e("MainViewModel", "checkFile Error:" + e.message)
        }

    }

    private fun getFileSelectStatus(path: String): Boolean {
        for (file in selectedFiles) {
            if (file.path == path)
                return true
        }
        return false
    }

    private val uriMap = mapOf(imageFlag to MediaStore.Images.Media.EXTERNAL_CONTENT_URI, videoFlag to MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
    private val orderMap = mapOf(imageFlag to MediaStore.Images.Media.DATE_MODIFIED, videoFlag to MediaStore.Video.Media.DATE_MODIFIED)
    private val dataColumnMap = mapOf(imageFlag to MediaStore.Images.Media.DATA, videoFlag to MediaStore.Video.Media.DATA)

    private fun getCurrentUri(): Uri {
        return uriMap.getOrElse(getPageFlag(), { MediaStore.Images.Media.EXTERNAL_CONTENT_URI });
    }

    private fun getCurrentOrder(): String {
        return orderMap.getOrElse(getPageFlag(), { MediaStore.Images.Media.DATE_MODIFIED })
    }

    private fun getCurrentMediaData(): String {
        return dataColumnMap.getOrElse(getPageFlag(), {MediaStore.Images.Media.DATA})
    }

    fun selectFolder(folder: Folder) {
        if (folder.id == currentFolder.value!!.id) return
        currentFolder.value!!.selected = false
        folder.selected = true
        if (totalFolderId == folder.id) {
            currentFolder.value = folder
            files.clear()
            files.addAll(allFiles)
            return
        }
        var flag = getPageFlag()
        val mediaUri = getCurrentUri()
        val sortOrder = getCurrentOrder()
        var column = getCurrentMediaData()
        var tempFiles = ArrayList<MyFile>()
        mDisPosable.add(Flowable.just(folder).flatMap { it ->

            mApplication!!.contentResolver.query(
                    mediaUri,
                null,
                    "$column like ?",
                arrayOf(it.dir + "%"),
                    sortOrder
            )?.let{cursor->
                val count = cursor!!.count
                for (i in count - 1 downTo 0) {
                    cursor.moveToPosition(i)
                    val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                    tempFiles.add(MyFile(File(path).name, path, getFileSelectStatus(path)))
                }
                cursor.close()
            }
            return@flatMap Flowable.just(true)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            currentFolder.value = folder
            files.clear()
            files.addAll(tempFiles)
        }, {
            result.value = "打开文件目录失败!"
            Log.e("MainViewModel","selectFolder failed! Error: " + it.message)
        }))

    }

    private fun scanFiles() {
        var flag = getPageFlag()
        val mediaUri = getCurrentUri()
        val sortOrder = getCurrentOrder()
        val cr: ContentResolver = mApplication!!.contentResolver
        val cursor = cr.query(
                mediaUri,
            null,
            null,
            null,
            sortOrder
        )
        var folderId = 0
        val dirPaths: MutableSet<String> = HashSet()
        val count = cursor!!.count
        var column = getCurrentMediaData()
        for (i in count - 1 downTo 0) {
            cursor.moveToPosition(i)
            val path = cursor.getString(cursor.getColumnIndex(column))
            if (i == count - 1) {
                val folder = Folder(folderId, "",path,if(pageFlag.value == imageFlag) "所有图片" else "所有视频", count,true,0)
                totalFolderId = folder.id
                tempFolders.add(folder)
                folderId += 1
            }
            val fileBean = MyFile(File(path).name, path, false)
            allFiles.add(fileBean)
            val parentFile = File(path).parentFile ?: continue
            val dirPath = parentFile.absolutePath
            if (dirPaths.contains(dirPath)) {
                continue
            } else {
                dirPaths.add(dirPath)
                var folder = Folder(folderId, dirPath, path, parentFile.name, 0, false, 1)
                if (parentFile.name == "0") {
                    // parentFile.name为"0"代表外存储根目录
                    // TODO: 还需要测试下插sd卡的手机, sd卡中图片的根目录是多少
                    folder.dirName = "外部存储"
                }
                tempFolders.add(folder)
                folderId += 1

                cr.query(
                        mediaUri,
                    null,
                        "$column like ?",
                    arrayOf("$dirPath%"),
                        sortOrder
                )?.count?.let{
                    folder.size = it
                }
            }
        }
        cursor.close()
    }

    inner class NullAlbumDirectoryException(message: String): Exception(message)

    companion object {
        private var instance = MainViewModel()
        fun getInstance() = instance
    }
}