package org.binar.soundpoolandmediaplayer

import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    // Maximumn sound stream.
    private val MAX_STREAMS = 1

    //Soundpool as player
    private lateinit var soundPool: SoundPool

    //Loaded is state of sound loaded into soundpool
    private var loaded = false

    //Sound ID is id created by SoundPool
    // And Sound ID needed to play the sound
    private var soundId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnVideoFromRaw.setOnClickListener {
            val goToVideoFromRawActivity = Intent(this, VideoFromRawActivity::class.java)
            startActivity(goToVideoFromRawActivity)
        }

        btnVideoFromInternet.setOnClickListener {
            val goToVideoFromInternetActivity = Intent(this, VideoFromInternetActivity::class.java)
            startActivity(goToVideoFromInternetActivity)
        }
        

        // For Android SDK >= 21
        if (Build.VERSION.SDK_INT >= 21) {
            val audioAttrib = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            val builder = SoundPool.Builder()
            builder.setAudioAttributes(audioAttrib).setMaxStreams(MAX_STREAMS)
            this.soundPool = builder.build()
        } else {
            // SoundPool(int maxStreams, int streamType, int srcQuality)
            this.soundPool = SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0)
        }

        this.soundPool.setOnLoadCompleteListener { soundPool, i, i2 ->
            loaded = true
        }

        this.soundId = this.soundPool.load(this, R.raw.gun, 1)

        btnPlay.setOnClickListener {

            //Get Sound Settings From System
            val mgr = getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val actualVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()
            val maxVolume = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()
            val volume = actualVolume / maxVolume

            if(loaded){
                val streamId = this.soundPool.play(this.soundId, volume, volume, 1, 0, 1f)
            }else{
                Toast.makeText(this, "Soundpool Not Loaded", Toast.LENGTH_LONG).show()
            }
        }
    }
}