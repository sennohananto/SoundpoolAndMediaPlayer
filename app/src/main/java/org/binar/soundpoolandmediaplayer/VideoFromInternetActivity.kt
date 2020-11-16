package org.binar.soundpoolandmediaplayer

import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaPlayer.OnPreparedListener
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.webkit.URLUtil
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_video_from_internet.*


class VideoFromInternetActivity : AppCompatActivity() {
    //URL Video from Internet
    private val VIDEO_SAMPLE = "https://developers.google.com/training/images/tacoma_narrows.mp4"

    // Current playback position (in milliseconds).
    private var mCurrentPosition = 0

    // Tag for the instance state bundle.
    private val PLAYBACK_TIME = "play_time"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_from_internet)

        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(PLAYBACK_TIME);
        }

        // Set up the media controller widget and attach it to the video view.
        val controller = MediaController(this)
        controller.setMediaPlayer(videoView)
        videoView.setMediaController(controller)
    }



    override fun onPause() {
        super.onPause()

        // In Android versions less than N (7.0, API 24), onPause() is the
        // end of the visual lifecycle of the app.  Pausing the video here
        // prevents the sound from continuing to play even after the app
        // disappears.
        //
        // This is not a problem for more recent versions of Android because
        // onStop() is now the end of the visual lifecycle, and that is where
        // most of the app teardown should take place.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            videoView.pause()
        }
    }

    override fun onStart() {
        super.onStart()
        initializePlayer()
    }



    override fun onStop() {
        super.onStop()

        // Media playback takes a lot of resources, so everything should be
        // stopped and released at this time.
        releasePlayer()
    }

    private fun releasePlayer() {
        videoView.stopPlayback()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Save the current playback position (in milliseconds) to the
        // instance state bundle.
        outState.putInt(PLAYBACK_TIME, videoView.getCurrentPosition());
    }

    fun initializePlayer(){
        // Show the "Buffering..." message while the video loads.
        tvBuffering.visibility = VideoView.VISIBLE

        // Buffer and decode the video sample.
        val videoUri: Uri = getMedia(VIDEO_SAMPLE)
        videoView.setVideoURI(videoUri)

        // Listener for onPrepared() event (runs after the media is prepared).
        videoView.setOnPreparedListener(
            OnPreparedListener { // Hide buffering message.
                tvBuffering.visibility = VideoView.INVISIBLE
                // Restore saved position, if available.
                if (mCurrentPosition > 0) {
                    videoView.seekTo(mCurrentPosition)
                } else {
                    // Skipping to 1 shows the first frame of the video.
                    videoView.seekTo(1)
                }
                // Start playing!
                videoView.start()
            })

        // Listener for onCompletion() event (runs after media has finished playing).
        videoView.setOnCompletionListener(
            OnCompletionListener {
                Toast.makeText(this, "Video Playback Completed",
                    Toast.LENGTH_SHORT
                ).show()

                // Return the video position to the start.
                videoView.seekTo(0)
            })
    }

    // Release all media-related resources. In a more complicated app this
    // might involve unregistering listeners or releasing audio focus.


    // Get a Uri for the media sample regardless of whether that sample is
    // embedded in the app resources or available on the internet.
    private fun getMedia(mediaName: String): Uri {
        return if (URLUtil.isValidUrl(mediaName)) {
            // Media name is an external URL.
            Uri.parse(mediaName)
        } else {

            // you can also put a video file in raw package and get file from there as shown below
            Uri.parse(
                "android.resource://" + packageName +
                        "/raw/" + mediaName
            )
        }
    }
}