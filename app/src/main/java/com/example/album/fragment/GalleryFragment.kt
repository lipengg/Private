package com.example.album.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.album.R
import com.example.album.adapter.ImageAdapter
import com.example.album.base.BaseBindingFragment
import com.example.album.databinding.FragmentAlbumBinding
import com.example.album.databinding.FragmentGalleryBinding
import com.example.album.dialog.AlbumDialog
import com.example.viewmodel.AlbumViewModel
import com.example.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_album.*
import kotlinx.android.synthetic.main.fragment_gallery.*

class GalleryFragment : BaseBindingFragment<FragmentGalleryBinding, MainViewModel>() {



    override fun getViewModel() = MainViewModel.getInstance()

    override fun getResourceLayout() = R.layout.fragment_gallery

    override fun initView(root: View) {
        iv_current_gallery.setOnClickListener{
            showAlbumDialog()
        }
        initRecycleView()
    }

    private var imageAdapter: ImageAdapter? = null
    private fun initRecycleView() {
        val layoutManager = GridLayoutManager(context, SPAN_COUNT, GridLayoutManager.VERTICAL, false)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_image_list.layoutManager = layoutManager
        imageAdapter = ImageAdapter(mBinding!!.viewModel!!.images)
        rv_image_list.adapter = imageAdapter
    }


    override fun initData(savedInstanceState: Bundle?) {
        mViewModel!!.getImageList()
    }

    override fun setViewModel(binding: FragmentGalleryBinding, vm: MainViewModel) {
        binding.viewModel = vm
        binding.lifecycleOwner = this
    }

    var albumDialog: AlbumDialog? = null
    private fun showAlbumDialog() {
        if (albumDialog == null) {
            albumDialog = AlbumDialog(activity)
        }
        if (!albumDialog!!.isShowing) {
            albumDialog!!.show()
        }
    }

    companion object {
        const val SPAN_COUNT = 4
    }

}