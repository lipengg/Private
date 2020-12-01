package com.example.viewmodel.manager

import android.app.Application

class ViewModelManger(val application: Application) {
    companion object {
        lateinit var intance: ViewModelManger
        fun init(application: Application) {
            intance = ViewModelManger(application)
        }
    }
}