package com.example.fitnessproject.ui.fragments.tapluyen

import android.content.Intent
import com.example.fitnessproject.R
import com.example.fitnessproject.ui.BaseFragment
import com.example.fitnessproject.ui.activities.topicdetail.ListTopicDetailActivity
import com.example.fitnessproject.ui.fragments.tapluyen.adapter.AdapterPractice
import kotlinx.android.synthetic.main.fragment_practice.*

class PracticeFragment : BaseFragment<PracticeViewModel>() {
    override fun getLayoutId() = R.layout.fragment_practice

    override fun initScreen() {
    }

    override fun bindData() {
        super.bindData()
        viewModel.topicLiveData.observe(viewLifecycleOwner) { list ->
            rcvListPractice?.apply {
                adapter = AdapterPractice(topicList = list,
                    onTopicModelClicked = { topicModel ->
                        val intent = Intent(activity, ListTopicDetailActivity::class.java)
                        intent.putExtra(ListTopicDetailActivity.KEY_TOPIC_MODEL, topicModel)
                        startActivity(intent)
                    })
            }
        }
    }
}