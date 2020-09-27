package com.example.album.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.album.R
import com.example.album.base.BaseBindingFragment
import com.example.album.databinding.FragmentGalleryBinding
import com.example.album.databinding.FragmentMainBinding
import com.example.viewmodel.AlbumViewModel
import com.example.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : BaseBindingFragment<FragmentMainBinding, MainViewModel>() {

    override fun getViewModel() = MainViewModel.getInstance().initial(mApplication)

    override fun getResourceLayout() = R.layout.fragment_main

    override fun initView(root: View) {
        iv_add_image.setOnClickListener { findNavController().navigate(R.id.action_mainFragment_to_galleryFragment) }
    }

    override fun setViewModel(binding: FragmentMainBinding, vm: MainViewModel) {
        binding.viewModel = vm
        binding.lifecycleOwner = this
    }

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = AlbumViewModel(requireActivity().application)
    }*/

}