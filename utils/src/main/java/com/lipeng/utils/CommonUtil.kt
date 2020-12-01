package com.lipeng.utils

import android.os.Environment

class CommonUtil {
    companion object {
        private val privateDirectory = Environment.getExternalStorageDirectory().absolutePath + "/PrivateAlbum/"
        fun getPrivateDirectory(): String {
            return privateDirectory
        }
    }
}