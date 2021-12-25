package com.example.album.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.album.R
import com.example.album.adapter.AlbumAdapter
import com.example.album.adapter.ImageAdapter
import com.example.album.base.BaseBindingFragment
import com.example.album.databinding.FragmentGalleryBinding
import com.example.model.bean.File
import com.example.model.bean.Folder
import com.example.model.bean.Image
import com.example.model.bean.ImageFolder
import com.example.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_gallery.*

class GalleryFragment : BaseBindingFragment<FragmentGalleryBinding, MainViewModel>() {

    override fun getViewModel() = MainViewModel.getInstance()

    override fun getResourceLayout() = R.layout.fragment_gallery

    override fun initView(root: View) {
        tv_current_gallery.setOnClickListener{
            showAlbumList()
        }
        iv_cancel.setOnClickListener{
            cancel()
        }
        btn_select_total.setOnClickListener{
            //mViewModel.encrypt()
            findNavController().popBackStack()
        }
        initRecycleView()
    }

    private var imageAdapter: ImageAdapter? = null
    private fun initRecycleView() {
        val layoutManager = GridLayoutManager(context, SPAN_COUNT, GridLayoutManager.VERTICAL, false)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_image_list.layoutManager = layoutManager
        imageAdapter = ImageAdapter(mViewModel.files, object: ImageAdapter.OnClickListener{
            override fun show(image: File) {
                showImage(image)
            }

        }, object : ImageAdapter.OnCheckedChangeListener {
            override fun onCheckedChanged(image: File, isChecked: Boolean) {
                mViewModel.checkFile(image, isChecked)
            }
        })
        rv_image_list.adapter = imageAdapter
    }

    fun showImage(image: File) {
        var bundle = Bundle()
        bundle.putString("path", image.path)
        var navController = findNavController()
        navController.navigate(R.id.action_galleryFragment_to_imageFragment4, bundle)
    }


    override fun initData(savedInstanceState: Bundle?) {
        mViewModel.currentFolder.observe(this, Observer {
            refreshAlbumList()
        })
    }

    override fun setViewModel(binding: FragmentGalleryBinding, vm: MainViewModel) {
        binding.viewModel = vm
        binding.lifecycleOwner = this
    }

    private fun cancel() {
        if (albumPopupWindow != null && albumPopupWindow!!.isShowing) {
            albumPopupWindow!!.dismiss()
            return
        }
        findNavController().popBackStack()
    }

    private var albumPopupWindow: PopupWindow? = null
    private var albumAdapter: AlbumAdapter? = null
    private fun showAlbumList() {
        if (albumPopupWindow == null) {
            var root = LayoutInflater.from(requireContext()).inflate(R.layout.pop_album, null)
            var albumList = root.findViewById<RecyclerView>(R.id.rv_gallery_list)
            if (albumAdapter == null) {
                albumAdapter = AlbumAdapter(MainViewModel.getInstance().folders, object: AlbumAdapter.OnSelectListener{
                    override fun select(album: Folder) {
                        MainViewModel.getInstance().selectFolder(album)
                        albumPopupWindow?.dismiss()
                    }

                })
            }
            val layoutManager = LinearLayoutManager(context)
            layoutManager.orientation = LinearLayoutManager.VERTICAL
            albumList.layoutManager = layoutManager
            albumList.adapter = albumAdapter


            albumPopupWindow = PopupWindow(root, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            albumPopupWindow?.run{
                setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), android.R.color.transparent))
                isFocusable = true
                isOutsideTouchable = true
            }
        }
        albumPopupWindow?.run{
            if (!isShowing) {
                showAsDropDown(cl_gallery_top)
            }
        }
    }

    private fun refreshAlbumList() {
        albumAdapter?.notifyDataSetChanged()
    }

    companion object {
        const val SPAN_COUNT = 4
    }

}