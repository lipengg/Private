package com.example.album.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.album.R
import com.example.album.adapter.PrivateImageAdapter
import com.example.album.base.BaseBindingFragment
import com.example.album.databinding.FragmentMainBinding
import com.example.model.bean.PrivateImage
import com.example.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : BaseBindingFragment<FragmentMainBinding, MainViewModel>() {

    override fun getViewModel() = MainViewModel.getInstance()

    override fun getResourceLayout() = R.layout.fragment_main

    override fun initView(root: View) {
        iv_add_image.setOnClickListener {
            findNavController().navigate(R.id.galleryFragment)
        }
        initRecycleView()
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        mViewModel.encrypt()
    }

    override fun setViewModel(binding: FragmentMainBinding, vm: MainViewModel) {
        binding.viewModel = vm
        binding.lifecycleOwner = this
    }

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = AlbumViewModel(requireActivity().application)
    }*/

    private var imageAdapter: PrivateImageAdapter? = null
    private fun initRecycleView() {
        val layoutManager = GridLayoutManager(context, SPAN_COUNT, GridLayoutManager.VERTICAL, false)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_selected_image_list.layoutManager = layoutManager
        imageAdapter = PrivateImageAdapter(mViewModel.encryptImages, object: PrivateImageAdapter.OnClickListener{
            override fun show(image: PrivateImage) {
                showImage(image)
            }
        }, null)
        rv_selected_image_list.adapter = imageAdapter
    }

    fun showImage(image: PrivateImage) {
        var bundle = Bundle()
        bundle.putString("path", image.getFilePath())
        findNavController().navigate(R.id.imageFragment, bundle)
    }
    companion object {
        const val SPAN_COUNT = 4
    }
}