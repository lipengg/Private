package com.example.album.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.viewmodel.base.BaseViewModel


abstract class BaseBindingFragment<DB : ViewDataBinding, VM : BaseViewModel> : BaseFragment() {
    protected lateinit var mViewModel: VM
    protected lateinit var mBinding: DB

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       var root = super.onCreateView(inflater, container, savedInstanceState)
        mBinding = DataBindingUtil.bind(root!!)!!
        setViewModel(mBinding,mViewModel)
        return root
    }

    abstract fun getViewModel(): VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = getViewModel()
    }

    abstract fun setViewModel(binding : DB,vm : VM)

    open fun sharedModelView() = false

    override fun onDestroyView() {
        mBinding.unbind()
        if(!sharedModelView()) {
            mViewModel.onDestory()
        }
        super.onDestroyView()
    }
}