package com.example.viewmodel.utils

import android.app.Application
import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.lipeng.utils.CommonUtil
import java.io.*
import java.nio.channels.FileChannel
import java.util.*


class FileUtils {
    companion object {
        fun getPrivateDirectory(): String {
            return CommonUtil.getPrivateDirectory()
        }

        fun getImagePrivateFileName(): String {
            return UUID.randomUUID().toString() + ".image.private"
        }

        fun deleteFile(path: String) {
            var file = File(path)
            file.delete()
        }

        fun renameFile(src: String, dest: String): Boolean {
            var result = false
            if (src.isEmpty() or dest.isEmpty()) {
                return result
            }

            val destFile = File(dest)
            if (destFile != null && destFile.exists()) {
                destFile.delete() // delete file
            }

            val srcFile = File(src)

            return srcFile.renameTo(destFile)
        }
    }
}