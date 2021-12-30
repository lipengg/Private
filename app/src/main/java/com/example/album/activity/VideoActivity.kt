package com.example.album.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import com.example.album.R
import kotlinx.android.synthetic.main.activity_video.*

class VideoActivity : AppCompatActivity() {

    var TAG = "VideoActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        intent?.extras?.let{
            val path = it.getString("path")
            try {
                path?.let {
                    Log.i(TAG, "video path $path")
                    //Creating MediaController
                    val mediaController = MediaController(this, false)
                    mediaController.setAnchorView(videoView_activity)
                    mediaController.setMediaPlayer(videoView_activity)
                    //specify the location of media file
                    val uri: Uri = Uri.parse(path)
                    //Setting MediaController and URI, then starting the videoView
                    videoView_activity.setMediaController(mediaController)
                    videoView_activity.setVideoURI(uri)
                    videoView_activity.requestFocus()
                    videoView_activity.start()
                }
            } catch (e: Exception) {
                Log.e(TAG, "player video error: " + e.message)
            }
        }
    }
}