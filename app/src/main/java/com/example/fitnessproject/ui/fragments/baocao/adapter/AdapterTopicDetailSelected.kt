package com.example.fitnessproject.ui.fragments.baocao.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessproject.R
import com.example.fitnessproject.domain.model.TopicDetailModel
import kotlinx.android.synthetic.main.layout_item_topic_selected.view.*

class AdapterTopicDetailSelected(private val list: ArrayList<TopicDetailModel>) :
    RecyclerView.Adapter<AdapterTopicDetailSelected.TopicDetailSelectedHolder>() {
    class TopicDetailSelectedHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicDetailSelectedHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TopicDetailSelectedHolder(
            inflater.inflate(
                R.layout.layout_item_topic_selected,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TopicDetailSelectedHolder, position: Int) {
        val item = list[position]
        holder.itemView.txtNameTopicDetailSelected.text = item.name.uppercase()
    }

    override fun getItemCount() = list.size
}