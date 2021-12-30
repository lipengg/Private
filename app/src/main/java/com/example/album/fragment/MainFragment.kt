package com.example.album.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.album.activity.MainActivity
import com.example.album.R
import com.example.album.adapter.PrivateImageAdapter
import com.example.album.base.BaseBindingFragment
import com.example.album.databinding.FragmentMainBinding
import com.example.model.bean.PrivateFile
import com.example.model.bean.PrivateFileType
import com.example.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : BaseBindingFragment<FragmentMainBinding, MainViewModel>() {

    override fun getViewModel() = MainViewModel.getInstance()

    override fun getResourceLayout() = R.layout.fragment_main

    override fun initView(root: View) {
        iv_add_image.setOnClickListener {
            mViewModel.loadFileList()
        }
        btn_recover_encrypt_file.setOnClickListener{
            if(mViewModel.selectNumber.value == 1) {
                mViewModel.decode()
            } else {
                mViewModel.switchModel()
            }
        }
        initRecycleView()
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        mViewModel.loadFileListResult.observe(this, Observer {
            if (it) {
                findNavController().navigate(R.id.galleryFragment)
                mViewModel.loadFileListResult.value = false
            }
        })
        mViewModel.pageModel.observe(this, Observer {
            imageAdapter?.notifyDataSetChanged()
            (activity as MainActivity).setBottomNavigationVisibility(it == mViewModel.viewModel)
        })
        mViewModel.encryptResult.observe(this, Observer {
            if(mViewModel.pageModel.value == 1) {
                for(file in mViewModel.selectedPrivateFiles) {
                    activity?.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.originPath)))
                }
                mViewModel.switchModel()
            } else {
                for(file in mViewModel.selectedFiles) {
                    activity?.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.path)))
                }
            }
//            activity?.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory())))
            mViewModel.resetSelectFile()
        })
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
        imageAdapter = PrivateImageAdapter(mViewModel.encryptFiles, object: PrivateImageAdapter.OnClickListener{
            override fun show(privateFile: PrivateFile) {
                Log.i("show PrivateFile", "${privateFile.id}|${privateFile.name}|${privateFile.type}|${privateFile.originPath}|${privateFile.originName}")
                if(privateFile.type == PrivateFileType.IMAGE.value)
                    showImage(privateFile)
                else if(privateFile.type == PrivateFileType.VIDEO.value)
                    playVideo(privateFile)
            }
        }, object: PrivateImageAdapter.OnCheckedChangeListener {
            override fun onCheckedChanged(file: PrivateFile, isChecked: Boolean) {
                mViewModel.checkPrivateFile(file, isChecked)
            }
        }, object: PrivateImageAdapter.OnLongClickListener {
            override fun enterEditModel(file: PrivateFile) {
                // 如果当前在查看模式长按进入编码模式, 已经是编辑模式长按无效
                if(mViewModel.pageModel.value == 0) {
                    mViewModel.switchModel()
                    //mViewModel.checkPrivateFile(file, true)
                }

            }
        })
        rv_selected_image_list.adapter = imageAdapter
    }

    fun showImage(image: PrivateFile) {
        var bundle = Bundle()
        bundle.putString("path", image.getFilePath())
        findNavController().navigate(R.id.imageFragment, bundle)
    }

    fun playVideo(video: PrivateFile) {
        var bundle = Bundle()
        bundle.putString("path", video.getFilePath())
        findNavController().navigate(R.id.videoActivity, bundle)
    }

    override fun onDestroyView() {
        mViewModel.loadFileListResult.removeObservers(this);
        super.onDestroyView()
    }

    companion object {
        const val SPAN_COUNT = 4
    }
}