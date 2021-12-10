package com.example.album

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

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
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bottom_nav_video -> {
                    Log.e(TAG, "open video page: ")
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }
}