package com.example.album.fragment

import android.net.Uri
import android.net.Uri.parse
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.Fragment
import com.example.album.activity.MainActivity
import com.example.album.R
import kotlinx.android.synthetic.main.fragment_video.*


class VideoFragment : Fragment() {
    var TAG = "VideoFragment"

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setBottomNavigationVisibility(false)
        try {
            var path = arguments?.getString("path")
            path?.let {
                Log.i(TAG, "video path $path")
                //Creating MediaController
                val mediaController = MediaController(activity, false)
                mediaController.setAnchorView(videoView)
                mediaController.setMediaPlayer(videoView)
                //specify the location of media file
                val uri: Uri = parse(path)
                //Setting MediaController and URI, then starting the videoView
                videoView.setMediaController(mediaController)
                videoView.setVideoURI(uri)
                videoView.requestFocus()
                videoView.start()
            }
        } catch (e: Exception) {
            Log.e(TAG, "player video error: " + e.message)
        }

    }
}