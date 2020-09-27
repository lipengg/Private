package com.example.viewmodel.base

import android.app.Application
import androidx.databinding.BaseObservable
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel(var mApplication: Application? = null) : BaseObservable() {
    protected var mDisPosable = CompositeDisposable()
    open fun onDestory() {
        mDisPosable.clear()
    }
}