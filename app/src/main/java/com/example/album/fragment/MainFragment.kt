package com.example.album.fragment

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.album.R
import com.example.album.adapter.PrivateImageAdapter
import com.example.album.base.BaseBindingFragment
import com.example.album.databinding.FragmentMainBinding
import com.example.model.bean.PrivateFile
import com.example.model.bean.PrivateImage
import com.example.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : BaseBindingFragment<FragmentMainBinding, MainViewModel>() {

    override fun getViewModel() = MainViewModel.getInstance()

    override fun getResourceLayout() = R.layout.fragment_main

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var root = super.onCreateView(inflater, container, savedInstanceState)


//        //监听返回键
//        view?.isFocusableInTouchMode = true;
//        view?.requestFocus();
//        view?.setOnKeyListener { _, i, keyEvent ->
//            if (keyEvent.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_BACK && mViewModel.pageModel.value == 1) {
//                mViewModel.switchModel()
//                return@setOnKeyListener true
//            }
//            return@setOnKeyListener false
//        }

        return root;
    }

    override fun initView(root: View) {
        iv_add_image.setOnClickListener {
            mViewModel.loadFileList()
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
            override fun show(image: PrivateFile) {
                showImage(image)
            }
        }, object: PrivateImageAdapter.OnCheckedChangeListener {
            override fun onCheckedChanged(file: PrivateFile, isChecked: Boolean) {
                mViewModel.checkPrivateFile(file, isChecked)
            }
        }, object: PrivateImageAdapter.OnLongClickListener {
            override fun enterEditModel() {
                // 如果当前在查看模式长按进入编码模式, 已经是编辑模式长按无效
                if(mViewModel.pageModel.value == 0) {
                    mViewModel.switchModel()
                    imageAdapter?.notifyDataSetChanged()
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

    override fun onDestroyView() {
        mViewModel.loadFileListResult.removeObservers(this);
        super.onDestroyView()
    }

    companion object {
        const val SPAN_COUNT = 4
    }
}