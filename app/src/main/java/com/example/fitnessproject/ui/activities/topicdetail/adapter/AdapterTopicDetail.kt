package com.example.fitnessproject.ui.activities.topicdetail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessproject.R
import com.example.fitnessproject.domain.model.TopicDetailModel
import kotlinx.android.synthetic.main.layout_item_topic_detail.view.*

class AdapterTopicDetail(
    private val list: List<TopicDetailModel>,
    private val onItemTopicDetailClicked: ((TopicDetailModel) -> Unit)
) :
    RecyclerView.Adapter<AdapterTopicDetail.TopicDetailHolder>() {
    class TopicDetailHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicDetailHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TopicDetailHolder(inflater.inflate(R.layout.layout_item_topic_detail, parent, false))
    }

    override fun onBindViewHolder(holder: TopicDetailHolder, position: Int) {
        val detail = list[position]
        holder.itemView.txtNameTopicDetail?.text = detail.name.uppercase()
        holder.itemView.itemDetail?.setOnClickListener {
            onItemTopicDetailClicked.invoke(detail)
        }
    }

    override fun getItemCount() = list.size
}