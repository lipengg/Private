package com.example.album.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.example.album.R

abstract class BaseDialog: Dialog {
    //返回布局ID
    abstract fun getResourceLayout(): Int

    //初始化view
    abstract fun initView()

    //初始化数据
    abstract fun initData()

    constructor(context: Context?, gravity: Int = Gravity.CENTER) : super(context!!) {
        val mWindow = window!!
        //mWindow.setBackgroundDrawableResource(R.drawable.transparent_bg)
        //var mLayoutParams: WindowManager.LayoutParams
        var mLayoutParams = mWindow.attributes
        mLayoutParams.alpha = 1f
        mLayoutParams.gravity = gravity
        //val view: View = View.inflate(context, R.layout.dialog_album, null)
        setContentView(View.inflate(context, getResourceLayout(), null))

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initial()
    }

    fun initial() {
        initView()
        initData()
    }
}