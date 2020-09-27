package com.example.album.base

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity


open abstract class BaseActivity : AppCompatActivity() {
    //返回布局ID
    abstract fun getResourceLayout(): Int

    //初始化view
    abstract fun initView()

    //初始化数据
    abstract fun initData(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initStateBar()
        setContentView(getResourceLayout())
        initView()
        initData(savedInstanceState)
    }

    private fun initStateBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            var window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.TRANSPARENT
            window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

/*    override fun onBackPressed() {
        if (useNavigation()) {
            super.onBackPressed()
            return
        }
        val backStackEntryCount = supportFragmentManager.backStackEntryCount
        if (backStackEntryCount == 0) {
            super.onBackPressed()
        } else {
            var fragments = supportFragmentManager.fragments
            var last = fragments.size - 1
            var fragment = fragments.elementAt(last)
            while (fragment is RequestManagerFragment || fragment is SupportRequestManagerFragment) {//过滤因为glide因为加载图片会向activity加载fragment
                last--
                fragment = fragments.elementAt(last)
            }
            if (fragment is BaseFragment) {
                (fragment).onBackPressed()
            } else {
                super.onBackPressed()
            }
        }

    }*/

    open fun fragmentChanged(layoutId: Int) {}

}