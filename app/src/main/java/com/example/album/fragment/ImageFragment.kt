package com.example.album.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.album.R
import kotlinx.android.synthetic.main.fragment_image.*
import java.io.File


class ImageFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var path = arguments?.getString("path")
        path?.let {
            Glide.with(requireActivity().applicationContext).load(File(it)).into(iv_image)
        }

    }

}