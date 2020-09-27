package com.example.album.base

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import com.example.viewmodel.base.BaseViewModel

abstract class BasicDialog<VM: BaseViewModel>: BaseDialog {
    protected lateinit var mViewModel: VM
    constructor(context: Context?, gravity: Int = Gravity.CENTER): super(context, gravity) {
        mViewModel = getViewModel()
    }

    abstract fun getViewModel(): VM

    override fun dismiss() {
        super.dismiss()
        mViewModel.onDestory()
    }
}