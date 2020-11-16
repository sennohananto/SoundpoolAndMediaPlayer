package org.binar.soundpoolandmediaplayer

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_video_from_raw.*


class VideoFromRawActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_from_raw)

        val path = "android.resource://$packageName/${R.raw.example_video_footage}"

        videoView.setVideoURI(Uri.parse(path))

        btnPlay.setOnClickListener {
            videoView.start()
        }

        btnPause.setOnClickListener {
            videoView.pause()
        }

        btnStop.setOnClickListener {
            videoView.stopPlayback()

            //After video stopped, need to re-set video URI.
            videoView.setVideoURI(Uri.parse(path))
        }
    }
}