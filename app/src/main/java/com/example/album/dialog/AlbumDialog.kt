package com.example.album.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Rect
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.album.R
import com.example.album.adapter.AlbumAdapter
import com.example.album.adapter.ImageAdapter
import com.example.album.fragment.GalleryFragment
import com.example.model.bean.ImageFolder
import com.example.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.dialog_album.*
import kotlinx.android.synthetic.main.fragment_gallery.*
import java.lang.ref.WeakReference


class AlbumDialog : Dialog {
    private lateinit var mLayoutParams: WindowManager.LayoutParams

    constructor(context: Context?) : super(context!!) {
        initView(context)
    }

    private fun initView(context: Context) {
        val mWindow = window!!
        mWindow.setBackgroundDrawableResource(R.drawable.transparent_bg)
        mLayoutParams = mWindow.attributes
        mLayoutParams.alpha = 1f
        mLayoutParams.gravity = Gravity.CENTER
        val view: View = View.inflate(context, R.layout.dialog_album, null)
        setContentView(view)
        setCancelable(true)
        setCanceledOnTouchOutside(false)
 /*       var lp = mWindow.attributes
        val outSize = Rect()
        window!!.windowManager.defaultDisplay.getRectSize(outSize)
        lp.width = outSize.width() - context.resources.getDimensionPixelSize(R.dimen.dialogPadding) * 2
        mWindow.attributes = lp*/

        initRecycleView()
    }

    private var albumAdapter: AlbumAdapter? = null
    private fun initRecycleView() {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_gallery_list.layoutManager = layoutManager
        albumAdapter = AlbumAdapter(MainViewModel.getInstance().imageFolders, object: AlbumAdapter.OnSelectListener{
            override fun select(album: ImageFolder) {
                MainViewModel.getInstance().selectAlbum(album)
                dismiss()
            }

        })
        rv_gallery_list.adapter = albumAdapter
    }

}