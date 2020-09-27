package com.example.album.base

import android.os.Bundle
import com.example.viewmodel.base.BaseViewModel

abstract class BasicActivity<VM: BaseViewModel> : BaseActivity() {
    protected lateinit var mViewModel: VM
    override fun onCreate(savedInstanceState: Bundle?) {
        mViewModel = getViewModel()
        super.onCreate(savedInstanceState)
    }

    abstract fun getViewModel(): VM
    override fun onDestroy() {
        mViewModel.onDestory()
        super.onDestroy()
    }
}