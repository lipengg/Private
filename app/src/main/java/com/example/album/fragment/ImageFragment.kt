package com.example.album.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.album.activity.MainActivity
import com.example.album.R
import kotlinx.android.synthetic.main.fragment_image.*
import java.io.File
import java.lang.Exception


class ImageFragment : Fragment() {

    var TAG = "ImageFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setBottomNavigationVisibility(false)
        try {
            var path = arguments?.getString("path")
            path?.let {
                Glide.with(requireActivity().applicationContext).load(File(it)).into(iv_image)
            }
        } catch (e:Exception) {
            Log.e(TAG, "Glide load image error: " + e.message)
        }

    }

}