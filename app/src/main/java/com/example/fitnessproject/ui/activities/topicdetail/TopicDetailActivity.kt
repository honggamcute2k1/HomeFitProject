package com.example.fitnessproject.ui.activities.topicdetail

import android.content.Intent
import android.net.Uri
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.fitnessproject.R
import com.example.fitnessproject.domain.model.TopicDetailModel
import com.example.fitnessproject.ui.BaseActivity
import com.example.fitnessproject.ui.activities.videodetail.VideoDetailActivity
import kotlinx.android.synthetic.main.activity_topic_detail.*

class TopicDetailActivity : BaseActivity<TopicDetailViewModel>() {
    override fun getLayoutId() = R.layout.activity_topic_detail

    companion object {
        const val KEY_TOPIC_DETAIL = "KEY_TOPIC_DETAIL"
    }

    override fun initScreen() {
        val topicDetail = intent.getParcelableExtra<TopicDetailModel>(KEY_TOPIC_DETAIL)
        topicDetail?.let { viewModel.getTopicDetail(topicDetail) }

        btnBack?.setOnClickListener {
            finish()
        }
        btnVideo?.setOnClickListener {
            val intent =
                Intent(this@TopicDetailActivity, VideoDetailActivity::class.java)
            intent.putExtra(VideoDetailActivity.KEY_TOPIC_DETAIL, topicDetail)
            startActivity(intent)
        }
    }

    override fun bindData() {
        super.bindData()
        viewModel.detailLiveData.observe(this) {
            Log.e("TAG", "TopicDetail $it")
            Glide.with(this)
                .load(Uri.parse("file:///android_asset/${it.tutorial}"))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imgTutorial)
            txtNameTopicDetail?.text = it.name.uppercase()
            txtDescription?.text = it.description
        }
    }
}