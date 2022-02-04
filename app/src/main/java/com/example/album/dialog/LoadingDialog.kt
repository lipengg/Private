package com.example.album.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import com.bumptech.glide.Glide
import com.example.album.R
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.dialog_view.*

class LoadingDialog(context: Context) : Dialog(context)  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.dialog_view)

        Glide.with(context).load(R.drawable.avd_active_loading).into(iv_loading)

        setCancelable(false)

    }
}