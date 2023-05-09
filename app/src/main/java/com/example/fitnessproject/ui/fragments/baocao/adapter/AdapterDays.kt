package com.example.fitnessproject.ui.fragments.baocao.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessproject.R
import com.example.fitnessproject.ui.showOrGone
import kotlinx.android.synthetic.main.layout_item_month.view.*

class AdapterDays(
    private val months: MutableList<Int>,
    private val onDayClicked: ((Int) -> Unit)
) :
    RecyclerView.Adapter<AdapterDays.DayHolder>() {
    private var index = -1

    class DayHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayHolder {
        val inflate = LayoutInflater.from(parent.context)
        return DayHolder(inflate.inflate(R.layout.layout_item_month, parent, false))
    }

    override fun onBindViewHolder(holder: DayHolder, position: Int) {
        val month = months[position]
        holder.itemView.txtItemMonth?.text = month.toString()
        holder.itemView.rootItemMonth?.setOnClickListener {
            index = holder.absoluteAdapterPosition
            onDayClicked.invoke(month)
            notifyDataSetChanged()
        }
        holder.itemView.viewMonthSelected?.showOrGone(index == position)
    }

    override fun getItemCount() = months.size
}