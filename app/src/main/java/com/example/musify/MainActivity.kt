package com.example.musify

import android.animation.ObjectAnimator
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.musify.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())
    private var progresss: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        mediaPlayer = MediaPlayer.create(this@MainActivity, R.raw.tu_hai_kahann)






        binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                progresss = progress
                if (fromUser) {
                    mediaPlayer?.seekTo(progress)
                }
                binding.textViewTimer.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        val initialTranslation = -binding.song.width.toFloat()
        binding.song.translationX = initialTranslation

// Adjust the final translation value based on your preference
        val finalTranslation = resources.displayMetrics.widthPixels.toFloat() - binding.song.width / 2

        val animator = ObjectAnimator.ofFloat(binding.song, "translationX", -820f, finalTranslation)
        animator.duration = 6500 // in milliseconds
        animator.repeatMode = ObjectAnimator.RESTART
        animator.repeatCount = ObjectAnimator.INFINITE
        animator.interpolator = LinearInterpolator()


        binding.play.setOnClickListener {
            binding.play.visibility = View.INVISIBLE
            binding.pause.visibility = View.VISIBLE
            mediaPlayer?.start()
            startUpdatingSeekBar()
            binding.song.visibility = View.VISIBLE
            animator.start()
        }

        binding.pause.setOnClickListener {
            binding.pause.visibility = View.INVISIBLE
            binding.play.visibility = View.VISIBLE
            mediaPlayer?.pause()
            handler.removeCallbacksAndMessages(null)
            animator.pause()
        }

        binding.rewind.setOnClickListener {
            if (progresss >= 10000) {
                mediaPlayer?.seekTo(progresss - 10000)
            }
        }

        binding.forward.setOnClickListener {
            if (progresss <= 253391) {
                mediaPlayer?.seekTo(progresss + 10000)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }

    private fun startUpdatingSeekBar() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (mediaPlayer?.isPlaying == true) {
                    updateSeekBar()
                }
                handler.postDelayed(this, 100) // Update every 100 milliseconds
            }
        }, 100)
    }

    private fun updateSeekBar() {
        runOnUiThread {
            if (mediaPlayer != null) {
                binding.seekbar.progress = mediaPlayer!!.currentPosition

            }
        }
    }
}
