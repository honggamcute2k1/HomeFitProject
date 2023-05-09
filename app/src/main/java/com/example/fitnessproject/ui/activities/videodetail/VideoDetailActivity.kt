package com.example.fitnessproject.ui.activities.videodetail

import android.content.Intent
import android.os.CountDownTimer
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.example.fitnessproject.R
import com.example.fitnessproject.domain.model.TopicDetailModel
import com.example.fitnessproject.ui.BaseActivity
import com.example.fitnessproject.ui.activities.main.MainActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.DefaultPlayerUiController
import kotlinx.android.synthetic.main.activity_video_detail.*


class VideoDetailActivity : BaseActivity<VideoDetailViewModel>() {
    override fun getLayoutId() = R.layout.activity_video_detail

    private var isPlay = false
    private var audio = true
    private var player: YouTubePlayer? = null
    private var topicDetailModel: TopicDetailModel? = null
    private var currentItem = -1
    private var topicDetailList = mutableListOf<TopicDetailModel>()
    private var timer: CountDownTimer? = null
    private var currentTimer = VALUE_TIMER


    companion object {
        const val VALUE_TIMER = 10000L
        const val STEP_COUNT_DOWN = 1000L
        const val KEY_TOPIC_DETAIL = "KEY_TOPIC_DETAIL"
    }

    private fun start(millisecond: Long) {
        timer = object : CountDownTimer(millisecond, STEP_COUNT_DOWN) {
            override fun onTick(millisUntilFinished: Long) {
                currentTimer = millisUntilFinished
                txtTimer?.text = (currentTimer / STEP_COUNT_DOWN).toInt().toString()
            }

            override fun onFinish() {
                txtTimer?.text = "0"
            }
        }.start()
    }

    private fun cancel() {
        timer?.cancel()
    }

    override fun initScreen() {
        topicDetailModel = intent.getParcelableExtra(KEY_TOPIC_DETAIL)
        topicDetailModel?.let { item ->
            viewModel.getListTopicDetail(item)
            txtNameTopicDetail?.text = item.name
        }

        btnPlay?.setOnClickListener {
            isPlay = !isPlay
            if (isPlay) {
                player?.play()
                start(currentTimer)
            } else {
                cancel()
                player?.pause()
            }
        }

        btnBack?.setOnClickListener {
            finish()
        }

        btnAudio?.setOnClickListener {
            audio = !audio
            if (audio) {
                player?.unMute()
            } else {
                player?.mute()
            }
        }

        btnNext?.setOnClickListener {
            getNextItem(isNext = true)
        }

        btnPrevious?.setOnClickListener {
            getNextItem(isNext = false)
        }
        youtubeView?.enableAutomaticInitialization = false
    }

    private fun setupLoadVideo() {
        cancel()
        currentTimer = VALUE_TIMER
        txtTimer.text = "10"
        topicDetailModel?.let { detail ->
            val videoId = detail.idVideo
            isPlay = false
            player?.cueVideo(videoId, 0f)
            txtNameTopicDetail?.text = detail.name
        }
    }

    override fun bindData() {
        super.bindData()
        viewModel.topicDetailLiveData.observe(this) { list ->
            topicDetailList.clear()
            topicDetailList.addAll(list)
            setupLayoutBtnNextOrPrevious()

            youtubeView?.initialize(object : AbstractYouTubePlayerListener() {
                override fun onStateChange(
                    youTubePlayer: YouTubePlayer,
                    state: PlayerConstants.PlayerState
                ) {
                    super.onStateChange(youTubePlayer, state)
                    if (state == PlayerConstants.PlayerState.ENDED) {
                        setupLoadVideo()
                        isPlay = false
                    } else if (state == PlayerConstants.PlayerState.VIDEO_CUED) {

                    }
                }

                override fun onReady(youTubePlayer: YouTubePlayer) {
                    player = youTubePlayer
                    val defaultPlayerUiController =
                        DefaultPlayerUiController(youtubeView, player!!)
                    defaultPlayerUiController.showUi(false)
                    youtubeView.setCustomPlayerUi(defaultPlayerUiController.rootView)
                    setupLoadVideo()
                    viewModel.showLoading(isShowLoading = false)
                }
            }, IFramePlayerOptions.Builder().controls(0).build())
        }
    }

    private fun setupLayoutBtnNextOrPrevious() {
        if (topicDetailList.size < 2) {
            btnPrevious?.isEnabled = false
            btnNext?.isEnabled = false
            return
        }
        val item = topicDetailList.firstOrNull { it.idDetail == topicDetailModel?.idDetail }
        item?.let {
            currentItem = topicDetailList.indexOf(it)
            when (currentItem) {
                0 -> {
                    enableButton(btnPrevious, false)
                    enableButton(btnNext, true)
                }
                topicDetailList.size - 1 -> {
                    enableButton(btnPrevious, true)
                    enableButton(btnNext, false)
                }
                else -> {
                    enableButton(btnPrevious, true)
                    enableButton(btnNext, true)
                }
            }
        }
    }

    private fun getNextItem(isNext: Boolean) {
        val afterIndex = if (isNext) {
            val nextItem = currentItem + 1
            if (nextItem >= topicDetailList.size) return
            nextItem
        } else {
            val previousItem = currentItem - 1
            if (previousItem < 0) return
            previousItem
        }
        topicDetailModel = topicDetailList[afterIndex]
        topicDetailModel?.let { model ->
            viewModel.insertTopicSelectedDetail(model)
        }
        setupLayoutBtnNextOrPrevious()
        setupLoadVideo()
    }

    private fun enableButton(view: ImageView, isEnable: Boolean) {
        if (isEnable) {
            view.isEnabled = true
            view.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )
        } else {
            view.isEnabled = false
            view.setColorFilter(
                ContextCompat.getColor(this, R.color.color_disable),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, QuestionExitActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onStop() {
        super.onStop()
        cancel()
        isPlay = false
    }

    override fun onDestroy() {
        super.onDestroy()
        youtubeView?.release()
        cancel()
    }
}