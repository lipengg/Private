package com.example.album.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.album.R
import com.example.album.adapter.ImageAdapter
import com.example.album.base.BaseBindingFragment
import com.example.album.databinding.FragmentGalleryBinding
import com.example.album.dialog.AlbumDialog
import com.example.model.bean.Image
import com.example.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_gallery.*

class GalleryFragment : BaseBindingFragment<FragmentGalleryBinding, MainViewModel>() {

    override fun getViewModel() = MainViewModel.getInstance()

    override fun getResourceLayout() = R.layout.fragment_gallery

    override fun initView(root: View) {
        iv_current_gallery.setOnClickListener{
            showAlbumDialog()
        }
        btn_select_total.setOnClickListener{
            mViewModel.encrypt()
        }
        initRecycleView()
    }

    private var imageAdapter: ImageAdapter? = null
    private fun initRecycleView() {
        val layoutManager = GridLayoutManager(context, SPAN_COUNT, GridLayoutManager.VERTICAL, false)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_image_list.layoutManager = layoutManager
        imageAdapter = ImageAdapter(mBinding!!.viewModel!!.images, object: ImageAdapter.OnClickListener{
            override fun show(image: Image) {
                showImage(image)
            }

        }, object : ImageAdapter.OnCheckedChangeListener {
            override fun onCheckedChanged(image: Image, isChecked: Boolean) {
                mViewModel.checkImage(image, isChecked)
            }
        })
        rv_image_list.adapter = imageAdapter
    }

    fun showImage(image: Image) {
        var bundle = Bundle()
        bundle.putString("path", image.path)
        var navController = findNavController()
        navController.navigate(R.id.action_galleryFragment_to_imageFragment4, bundle)
    }


    override fun initData(savedInstanceState: Bundle?) {
        mViewModel.encryptResult.observe(this, Observer {
            findNavController().popBackStack()
        })
    }

    override fun setViewModel(binding: FragmentGalleryBinding, vm: MainViewModel) {
        binding.viewModel = vm
        binding.lifecycleOwner = this
    }

    private var albumDialog: AlbumDialog? = null
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