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

        private fun deleteImage(path: String, application: Application) {
            var file = File(path)
            // Set up the projection (we only need the ID)
            val projection = arrayOf(MediaStore.Images.Media._ID)

            // Match on the file path
            val selection = MediaStore.Images.Media.DATA + " = ?"
            val selectionArgs = arrayOf<String>(file.absolutePath)

            // Query for the ID of the media matching the file path
            val queryUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val contentResolver: ContentResolver = application.contentResolver
            contentResolver.query(queryUri, projection, selection, selectionArgs, null)?.let {c->
                if (c.moveToFirst()) {
                    // We found the ID. Deleting the item via the content provider will also remove the file
                    val id: Long =
                        c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                    val deleteUri: Uri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                    contentResolver.delete(deleteUri, null, null)
                } else {
                    // File not found in media store DB
                }
                c.close()
            }
        }

        fun moveImage(src: String, dest: String, application: Application): Boolean {
            var result = false
            if (src.isEmpty() or dest.isEmpty()) {
                return result
            }

            val dest = File(dest)
            if (dest != null && dest.exists()) {
                dest.delete() // delete file
            }
            try {
                dest.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            var srcChannel: FileChannel? = null
            var dstChannel: FileChannel? = null

            try {
                srcChannel = FileInputStream(src).channel
                dstChannel = FileOutputStream(dest).channel
                srcChannel.transferTo(0, srcChannel.size(), dstChannel)
                result = true
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                return result
            } catch (e: IOException) {
                e.printStackTrace()
                return result
            }
            try {
                srcChannel.close()
                dstChannel.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if(result) {
                deleteImage(src, application)
            }
            return result
        }
    }
}