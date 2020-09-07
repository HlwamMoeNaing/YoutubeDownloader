@file:Suppress("DEPRECATION")

package com.hmn.movies.activity


import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.hmn.movies.R


@Suppress("DEPRECATION")
class PlayerActivity : AppCompatActivity() {
    lateinit var playerview: PlayerView
    private var player: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    lateinit var fullscreenButton:AppCompatImageView
    private var fullScreen = false

    private var player2: SimpleExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        playerview = findViewById(R.id.exo)
        player2 = ExoPlayerFactory.newSimpleInstance(applicationContext)

        fullscreenButton = findViewById(R.id.exo_fullscreen_icon)
        fullscreenButton.setOnClickListener {
            if(fullScreen) {
                fullscreenButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.full_screen_open
                    )
                )

                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE)

                if(supportActionBar != null){
                    supportActionBar!!.show()
                }

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)


                val params = playerview.layoutParams as (ConstraintLayout.LayoutParams)

                params.width=ViewGroup.LayoutParams.MATCH_PARENT
                params.height = ( 200 * getApplicationContext().getResources().getDisplayMetrics().density).toInt()
                playerview.setLayoutParams(params)

                fullScreen = false
            }else{
                fullscreenButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.full_screen_close
                    )
                )


                window.decorView.systemUiVisibility =View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION




                if(getSupportActionBar() != null){
                    getSupportActionBar()!!.hide()
                }

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)


                val params = playerview.layoutParams as (ConstraintLayout.LayoutParams)
                params.width=ViewGroup.LayoutParams.MATCH_PARENT
                params.height=ViewGroup.LayoutParams.MATCH_PARENT
                playerview.setLayoutParams(params)

                fullScreen = true
            }
        }


    }





    private fun initializePlayer() {
        player = SimpleExoPlayer.Builder(this).build()
        playerview.player = player
        playerview.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT
        val uri = intent.getStringExtra("video_uri")
        val mediaSource = buildMediaSource(Uri.parse(uri))
        player!!.playWhenReady = playWhenReady
        player!!.seekTo(currentWindow, playbackPosition)
        player!!.prepare(mediaSource!!, false, false)

    }


    private fun buildMediaSource(uri: Uri): MediaSource? {
        val dataSourceFactory: DataSource.Factory =
            DefaultDataSourceFactory(this, "Movies")
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)
    }

    private fun releasePlayer() {
        if (player != null) {
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            playWhenReady = player!!.playWhenReady
            player!!.release()
            player = null
        }
    }
    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    override fun onResume() {
        super.onResume()
        if ( player == null) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()

        releasePlayer()

    }

    override fun onStop() {
        super.onStop()
        releasePlayer()

    }

}