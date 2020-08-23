package com.devrock.beautyappv2.signup.onboarding

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Animatable
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.devrock.beautyappv2.AppActivity
import com.devrock.beautyappv2.R
import com.devrock.beautyappv2.databinding.FragmentOnboardingBinding
import com.devrock.beautyappv2.ui.DepthPageTransformer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.util.Util

class OnboardingFragment : Fragment() {

    private val viewModel: OnboardingViewModel by lazy {
        ViewModelProviders.of(activity!!).get(OnboardingViewModel::class.java)
    }

    private lateinit var binding: FragmentOnboardingBinding

    private lateinit var player : SimpleExoPlayer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentOnboardingBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel

       val args = OnboardingFragmentArgs.fromBundle(arguments!!)
        val session = args.session

        val videoPath = Uri.parse("https://storage.yandexcloud.net/beauty/onboarding.mp4")

        player = SimpleExoPlayer.Builder(context!!).build()

        binding.videoView.setShutterBackgroundColor(Color.TRANSPARENT)

        val dataSourceFactory = DefaultDataSourceFactory(context!!, Util.getUserAgent(context!!, "beautyappv2"))
        val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(videoPath)
        player.prepare(videoSource)

        binding.videoView.player = player

        player.playWhenReady = true

        player.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)
                if (playbackState == Player.STATE_ENDED) {
                    gotoAppActivity(session)
                }
            }
        })



        binding.left.setOnClickListener {
            val currentPos = player.currentPosition
            when {
                currentPos < 7000 -> {
                    player.seekTo(0)
                    Log.e("curpos", currentPos.toString())
                }
                currentPos in 7001..16000 -> {
                    player.seekTo(6000)
                    Log.e("curpos", currentPos.toString())
                }
                currentPos > 16000 -> {
                    player.seekTo(14500)
                    Log.e("curpos", currentPos.toString())
                }
            }
        }

        binding.right.setOnClickListener {
            val currentPos = player.currentPosition
            when {
                currentPos < 6000 -> {
                    Log.e("curpos", currentPos.toString())
                    player.seekTo(6000)
                }
                currentPos in 6001..14500 -> {
                    Log.e("curpos", currentPos.toString())
                    player.seekTo(14500)
                }
                currentPos > 14500 -> {
                    Log.e("curpos", currentPos.toString())
                }
            }
        }


        activity!!.onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })

        return binding.root
    }

    fun gotoAppActivity(session: String) {
        val intent = Intent(context, AppActivity::class.java)
        intent.putExtra("session", session)
        startActivity(intent)
    }

    override fun onPause() {
        super.onPause()
        player.release()
    }
}