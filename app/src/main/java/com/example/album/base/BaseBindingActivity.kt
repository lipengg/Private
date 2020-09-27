package com.example.album.base

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.viewmodel.base.BaseViewModel

open abstract class BaseBindingActivity<DB : ViewDataBinding, VM : BaseViewModel> : BaseActivity() {
    protected lateinit var mViewModel: VM
    protected lateinit var mBinding: DB
    var init = false//底层会调用setContentView 做下判断，否则递归
    override fun setContentView(layoutResID: Int) {
        if (!init) {
            init = true
            mBinding = DataBindingUtil.setContentView(this, layoutResID)
            mBinding.lifecycleOwner = this
        } else {
            super.setContentView(layoutResID)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = getViewModel()
    }

    abstract fun getViewModel(): VM

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.onDestory()
    }
}