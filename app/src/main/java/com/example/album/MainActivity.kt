package com.example.album

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    //var viewModel: MainViewModel? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //MainViewModel.getInstance().initial(application)
    }
}