package com.example.viewmodel

import android.app.Application
import com.example.viewmodel.base.BaseViewModel
import com.example.viewmodel.utils.FileUtils.Companion.getPrivateDirectory
import java.io.File

class SplashViewModel(application: Application): BaseViewModel(application) {
    init {
        val file = File(getPrivateDirectory())
        if (!file.exists()) {
            file.mkdirs()
        }
    }
}