package com.example.album.base

import android.os.Bundle
import com.example.viewmodel.base.BaseViewModel

abstract class BasicFragment<VM : BaseViewModel>: BaseFragment() {
    protected lateinit var mViewModel: VM
    override fun onCreate(savedInstanceState: Bundle?) {
        mViewModel = getViewModel()
        super.onCreate(savedInstanceState)
    }

    abstract fun getViewModel(): VM
    override fun onDestroyView() {
        mViewModel.onDestory()
        super.onDestroyView()
    }
}