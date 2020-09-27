package com.example.album.base

import android.animation.ObjectAnimator
import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference


abstract class BaseFragment : Fragment() {
    //返回布局ID
    abstract fun getResourceLayout(): Int

    //初始化view
    abstract fun initView(root: View)

    //初始化数据
    protected open fun initData(savedInstanceState: Bundle?) {
    }

    protected lateinit var mWeakActivity: WeakReference<Activity>
    protected var root: View? = null
    protected lateinit var mApplication: Application
    private var loginGlobalLayoutListener: GlobalLayoutListener? = null
    private var usableHeightPrevious = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mWeakActivity = WeakReference(activity as Activity)
        mApplication = requireContext().applicationContext as Application
    }


    //private var loadingDialog: LoadingDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(getResourceLayout(), container, false)
        view.setOnTouchListener { _, _ ->
            //hideKeyBord()
            true
        }//防止事件穿透
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.root = view
        initKeyBordListener()
        initView(view)
        initData(savedInstanceState)
        //(activity as BaseActivity).fragmentChanged(getResourceLayout())
    }

    protected open fun isListenKeyboard(): Boolean {
        return false
    }

    protected open fun getScrollDistence(): Int {
        return 220
    }

    private fun initKeyBordListener() {
        if (!isListenKeyboard()) {
            return
        }
        if (root != null) {
            loginGlobalLayoutListener = GlobalLayoutListener()
            root!!.viewTreeObserver!!.addOnGlobalLayoutListener(loginGlobalLayoutListener)
        }
    }

    private fun computeUsableHeight(): Int {
        val r = Rect()
        if (root != null) {
            root!!.getWindowVisibleDisplayFrame(r)
            return r?.bottom - r?.top// 全屏模式下： return r.bottom
        }
        return 0
    }

    fun adjustView() {
        if (root == null) {
            return
        }
        val usableHeightNow: Int = computeUsableHeight()
        if (usableHeightNow != usableHeightPrevious) {
            val height = root!!.height
            val usableHeightSansKeyboard: Int = height!!
            val heightDifference = usableHeightSansKeyboard - usableHeightNow
            if (heightDifference > usableHeightSansKeyboard / 4) {
                var animator = ObjectAnimator.ofFloat(root!!, "y", root!!.y, -getScrollDistence().toFloat()).setDuration(150)
                animator.start()
                //  root!!.scrollTo(0, getScrollDistence())
            } else {
                //root!!.scrollTo(0, 0)
                var animator = ObjectAnimator.ofFloat(root!!, "y", root!!.y, 0f).setDuration(150)
                animator.start()
            }
            usableHeightPrevious = usableHeightNow
        }
    }


    inner class GlobalLayoutListener : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            adjustView()
        }
    }

    companion object {
        @JvmStatic
        fun <F : Fragment> newInstance(clazz: Class<F>): F {
            return clazz.getConstructor().newInstance() as F
        }
    }

    override fun onDestroyView() {
        if (root != null && loginGlobalLayoutListener != null && root!!.viewTreeObserver != null) {
            root!!.viewTreeObserver.removeGlobalOnLayoutListener(loginGlobalLayoutListener)
        }
        if (root != null) {
            root!!.clearAnimation()
        }
        loginGlobalLayoutListener = null
        root = null
        super.onDestroyView()
    }


}