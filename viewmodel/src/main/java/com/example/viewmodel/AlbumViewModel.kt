package com.example.viewmodel

import android.app.Application
import android.content.ContentResolver
import android.os.Environment
import android.os.Message
import android.provider.MediaStore
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.example.model.bean.Image
import com.example.model.bean.ImageFolder
import com.example.model.bean.Photo
import com.example.viewmodel.base.BaseViewModel
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class AlbumViewModel(mApplication: Application): BaseViewModel(mApplication) {

}