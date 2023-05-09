package com.example.fitnessproject.ui.fragments.baocao.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessproject.R
import com.example.fitnessproject.domain.model.TopicDetailModel
import com.example.fitnessproject.ui.fragments.baocao.adapter.AdapterTopicDetailSelected

class DialogListTopicDetailSelected : DialogFragment() {

    companion object {
        private const val KEY_LIST = "KEY_LIST"

        fun getInstance(topicDetail: ArrayList<TopicDetailModel>): DialogListTopicDetailSelected {
            val dialog = DialogListTopicDetailSelected()
            val bundle = Bundle()
            bundle.putParcelableArrayList(KEY_LIST, topicDetail)
            dialog.arguments = bundle
            return dialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setContentView(R.layout.layout_dialog_topic_detail_selected)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            dialog.window?.setDecorFitsSystemWindows(false)
        } else {
            dialog.window?.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
            )
        }
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )

        arguments?.let { it ->
            dialog.findViewById<TextView>(R.id.btnOK)?.setOnClickListener {
                dialog.dismiss()

            }
            dialog.findViewById<RecyclerView>(R.id.rcvListTopicDetailSelected)?.apply {
                adapter = AdapterTopicDetailSelected(
                    list = it.getParcelableArrayList(
                        KEY_LIST
                    ) ?: arrayListOf()
                )
            }
        }
        return dialog
    }

}