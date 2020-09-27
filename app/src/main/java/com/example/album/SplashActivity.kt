package com.example.album

import android.Manifest
import android.annotation.TargetApi
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

class SplashActivity : AppCompatActivity() {

    private val REQUEST_CAMERA_CODE = 0x0002
    private val REQUEST_EXTERNAL_STORAGE_CODE = 0x0003
    private val REQUEST_CAMERA_RESULT_CODE = 0x0004
    private val REQUEST_CLICK_PHOTO_CODE = 0x0005

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // (API) void requestPermissions (Activity activity, String[] permissions, int requestCode)
            requestPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_EXTERNAL_STORAGE_CODE)
        } else {
            startMainActivity()
            finish()
        }
    }

    private fun checkPermission(@NonNull permission: String ): Boolean {
        return ContextCompat.checkSelfPermission(applicationContext, permission) != PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestPermissions(@NonNull permission: String, requestCode: Int) {
        requestPermissions(arrayOf(permission), requestCode)
    }

    private fun startMainActivity() {
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
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
                startMainActivity()
            } else {
                Toast.makeText(applicationContext, "未获取读取存储卡权限", Toast.LENGTH_SHORT).show()
                finish()
            }
            return
        }
    }
}