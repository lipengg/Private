package com.example.album.activity

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.album.R
import com.example.album.dialog.LoadingDialog
import com.example.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_view.*

class MainActivity : AppCompatActivity() {

    //var viewModel: MainViewModel? = null
    private val TAG = "MainActivity"

    lateinit var dialog : LoadingDialog

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        savedInstanceState?.let{arg->
            MainViewModel.getInstance().pageFlag.value = arg.getInt("flag")
        }
        Log.e(TAG, "pageFlag value is $MainViewModel.getInstance().pageFlag")

        MainViewModel.getInstance().result.observe(this, Observer {
            if (it.isNotEmpty()) {
                Toast.makeText(applicationContext, it, Toast.LENGTH_LONG).show()
            }
        })

        MainViewModel.getInstance().pageFlag.observe(this, Observer {
            MainViewModel.getInstance().reloadPageInfo()
        })

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav.setOnNavigationItemSelectedListener{item->
            when (item.itemId) {
                R.id.bottom_nav_image, R.id.bottom_nav_video -> {
                    val flag = if(item.itemId == R.id.bottom_nav_image) MainViewModel.getInstance().imageFlag else MainViewModel.getInstance().videoFlag
                    MainViewModel.getInstance().pageFlag.value = flag
//                    nav_main_fragment.findNavController().currentDestination?.let {
//                        if (it.id == R.id.mainFragment) {
//                            MainViewModel.getInstance().pageFlag.value = flag
//                        } else {
//                            val bundle = bundleOf("flag" to flag)
//                            findNavController(R.id.nav_main_fragment).navigate(
//                                R.id.mainFragment,
//                                bundle
//                            )
//                        }
//                    }
                    return@setOnNavigationItemSelectedListener true
                }

            }
            false
        }

        val leftNav = findViewById<NavigationView>(R.id.left_nav_view)
        leftNav.setNavigationItemSelectedListener { item->
            when (item.itemId) {
                R.id.recycleFragment -> {
                    findNavController(R.id.nav_main_fragment).navigate(
                        R.id.recycleFragment
                    )
                    findViewById<DrawerLayout>(R.id.draw_layout).closeDrawer(Gravity.LEFT)
                    return@setNavigationItemSelectedListener true
                }
            }
            false
        }


        dialog = LoadingDialog(this)
    }

    /**
     * 启动自定义的对话框
     * @param view
     */
    fun showLoadDialog() {
        dialog.show()
    }

    fun hideLoadDialog() {
        dialog.hide()
    }

    override fun onBackPressed() {
        MainViewModel.getInstance().resetSelectFile()
        if(MainViewModel.getInstance().pageModel.value == 1) {
            MainViewModel.getInstance().switchModel()
            return
        }
        super.onBackPressed()
    }

    fun setBottomNavigationVisibility(value: Boolean) {
        if(value)
            bottom_nav_view.visibility = View.VISIBLE
        else
            bottom_nav_view.visibility = View.GONE
    }
}