package com.example.fitnessproject.ui.fragments.baocao.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessproject.R
import com.example.fitnessproject.ui.fragments.baocao.adapter.AdapterDays
import com.example.fitnessproject.ui.getMonth
import java.util.*

class DialogAddWeight : DialogFragment() {

    var onSaveWeightClicked: ((Int, Double) -> Unit)? = null
    private var daySelected: Int = -1
    private var weight: Double? = null
    private var editTextWeight: EditText? = null

    companion object {
        const val KEY_MONTH = "KEY_MONTH"
        const val KEY_INFO = "KEY_INFO"

        fun getInstance(month: Int, list: ArrayList<String>): DialogAddWeight {
            val dialogAddWeight = DialogAddWeight()
            val bundle = Bundle()
            bundle.putInt(KEY_MONTH, month)
            bundle.putStringArrayList(KEY_INFO, list)
            dialogAddWeight.arguments = bundle
            return dialogAddWeight
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setContentView(R.layout.layout_dialog_add_weight)
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
        editTextWeight = dialog.findViewById(R.id.editTextCanNang)

        arguments?.let { it ->
            val month = it.getInt(KEY_MONTH)
            val stringList = it.getStringArrayList(KEY_INFO) ?: arrayListOf()
            val list = mutableListOf<Pair<Double, Int>>()
                .apply {
                    stringList.forEach { item ->
                        val array = item.split(":")
                        val weight = array[0].toDouble()
                        val day = array[1].toInt()
                        add(weight to day)
                    }
                }
            dialog.findViewById<TextView>(R.id.txtMonth)?.text = month.getMonth()

            dialog.findViewById<TextView>(R.id.btnCancel)?.setOnClickListener {
                dismiss()
            }
            dialog.findViewById<TextView>(R.id.btnSaveWeight)?.setOnClickListener {
                weight = editTextWeight?.text?.toString()?.toDouble()
                dismiss()
                if (daySelected < 0 || weight == null) {
                    return@setOnClickListener
                } else {
                    onSaveWeightClicked?.invoke(daySelected, weight!!)
                }

            }
            dialog.findViewById<RecyclerView>(R.id.rcvDayOfMonth)?.apply {
                adapter = AdapterDays(months = getListDayOfMonth(month),
                    onDayClicked = { day ->
                        daySelected = day
                        list.firstOrNull { it.second == daySelected }?.let {
                            editTextWeight?.setText(it.first.toString())
                        } ?: editTextWeight?.setText("")
                    })
            }
        }
        return dialog
    }


    fun getListDayOfMonth(month: Int): MutableList<Int> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.MONTH, month)
        return mutableListOf<Int>().apply {
            for (i in 1..calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                add(i)
            }
        }
    }
}