package com.example.album.activity

import android.Manifest
import android.annotation.TargetApi
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.album.R
import com.example.album.base.BasicActivity
import com.example.model.database.DatabaseManager
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.SplashViewModel
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BasicActivity<SplashViewModel>() {

    private val REQUEST_CAMERA_CODE = 0x0002
    private val REQUEST_EXTERNAL_STORAGE_CODE = 0x0003
    private val REQUEST_CAMERA_RESULT_CODE = 0x0004
    private val REQUEST_CLICK_PHOTO_CODE = 0x0005

    override fun getViewModel() = SplashViewModel(application)

    override fun getResourceLayout() = R.layout.activity_splash

    override fun initView() {
        Glide.with(applicationContext).load(R.drawable.avd_active_loading).into(iv_album_loading)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData(savedInstanceState: Bundle?) {
        if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE) && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // (API) void requestPermissions (Activity activity, String[] permissions, int requestCode)
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_EXTERNAL_STORAGE_CODE)
        } else {
            init()
        }
    }

    private fun init() {
        DatabaseManager.initDataBase(application)
        MainViewModel.getInstance().initial(application)
        MainViewModel.getInstance().initialled.observe(this, Observer {
            if(it) {
                startMainActivity()
            }
        })
    }

    private fun checkPermission(@NonNull permission: String ): Boolean {
        return ContextCompat.checkSelfPermission(applicationContext, permission) != PackageManager.PERMISSION_GRANTED
    }

    /*@RequiresApi(Build.VERSION_CODES.M)
    private fun requestPermissions(@NonNull permission: String, requestCode: Int) {
        requestPermissions(arrayOf(permission), requestCode)
    }*/

    private fun startMainActivity() {
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


    @TargetApi(23)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults!!)
        if (requestCode === REQUEST_EXTERNAL_STORAGE_CODE) {
            if (grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "已获取读取存储卡权限", Toast.LENGTH_SHORT).show()
                init()
            } else {
                Toast.makeText(applicationContext, "未获取读取存储卡权限", Toast.LENGTH_SHORT).show()
                finish()
            }
            return
        }
    }
}