package com.example.album.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.album.R
import kotlinx.android.synthetic.main.fragment_image.*


class ImageFragment : Fragment() {

    var path = arguments?.get("path")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        path?.let {
            Glide.with(requireActivity().applicationContext).load(it).into(iv_image)
        }

    }

}