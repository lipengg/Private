package com.example.album

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    //var viewModel: MainViewModel? = null
    private var TAG = "MainActivity"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MainViewModel.getInstance().result.observe(this, Observer {
            if (it.isNotEmpty()) {
                Toast.makeText(applicationContext, it, Toast.LENGTH_LONG).show()
            }
        })

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav.setOnNavigationItemSelectedListener{item->
            when (item.itemId) {
                R.id.bottom_nav_image -> {
                    Log.e(TAG, "open image page: ")
                    findNavController(R.id.nav_main_fragment).navigate(R.id.mainFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bottom_nav_video -> {
                    Log.e(TAG, "open video page: ")
                    findNavController(R.id.nav_main_fragment).navigate(R.id.mainFragment)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }

        val leftNav = findViewById<NavigationView>(R.id.left_nav_view)
        leftNav.setNavigationItemSelectedListener { item->
            when (item.itemId) {
                R.id.recycleFragment -> {
                    findNavController(R.id.nav_main_fragment).navigate(R.id.recycleFragment)
                    findViewById<DrawerLayout>(R.id.draw_layout).closeDrawer(Gravity.LEFT)
                    return@setNavigationItemSelectedListener true
                }
            }
            false
        }
    }
}