package com.example.fitnessproject.ui.fragments.tapluyen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessproject.R
import com.example.fitnessproject.domain.model.TopicModel
import com.example.fitnessproject.domain.model.TopicType
import kotlinx.android.synthetic.main.layout_item_practice.view.*

class AdapterPractice(
    private val topicList: List<TopicModel>,
    private var onTopicModelClicked: ((TopicModel) -> Unit)
) :
    RecyclerView.Adapter<AdapterPractice.PracticeHolder>() {

    class PracticeHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PracticeHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PracticeHolder(inflater.inflate(R.layout.layout_item_practice, parent, false))
    }

    override fun onBindViewHolder(holder: PracticeHolder, position: Int) {
        val item = topicList[position]
        holder.itemView.txtItemPractice?.text = item.name.uppercase()
        holder.itemView.imageItemPractice?.setImageResource(TopicType.valueOf(item.type).resource)
        holder.itemView.itemPractice?.setOnClickListener {
            onTopicModelClicked.invoke(item)
        }
    }

    override fun getItemCount() = topicList.size
}