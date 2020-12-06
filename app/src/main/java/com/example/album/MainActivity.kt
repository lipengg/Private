package com.example.album

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    //var viewModel: MainViewModel? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MainViewModel.getInstance().result.observe(this, Observer {
            if (it.isNotEmpty()) {
                Toast.makeText(applicationContext, it, Toast.LENGTH_LONG).show()
            }
        })
    }
}