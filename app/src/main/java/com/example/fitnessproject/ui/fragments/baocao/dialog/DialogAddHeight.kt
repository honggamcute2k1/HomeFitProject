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
import com.example.fitnessproject.R

class DialogAddHeight : DialogFragment() {
    private var editWeight: EditText? = null
    private var editHeight: EditText? = null
    var onSaveInformationClicked: ((Float, Float) -> Unit)? = null

    companion object {
        const val KEY_WEIGHT = "KEY_WEIGHT"
        const val KEY_HEIGHT = "KEY_HEIGHT"
        fun getInstance(weight: Double?, height: Double?): DialogAddHeight {
            val dialog = DialogAddHeight()
            val bundle = Bundle()
            weight?.let { bundle.putDouble(KEY_WEIGHT, it) }
            height?.let { bundle.putDouble(KEY_HEIGHT, it) }
            dialog.arguments = bundle
            return dialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setContentView(R.layout.layout_dialog_add_bmi)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val weight1 = arguments?.getDouble(KEY_WEIGHT)
        val height1 = arguments?.getDouble(KEY_HEIGHT)
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

        editHeight = dialog.findViewById(R.id.editTextHeight)
        editWeight = dialog.findViewById(R.id.editTextWeight)
        if (weight1 != null && height1 != null) {
            editHeight?.setText(height1.toInt().toString())
            editWeight?.setText(weight1.toInt().toString())
        }
        dialog.findViewById<TextView>(R.id.btnCancel)?.setOnClickListener {
            dismiss()
        }
        dialog.findViewById<TextView>(R.id.btnSaveWeight).setOnClickListener {
            val height = editHeight?.text?.toString() ?: ""
            val weight = editWeight?.text?.toString() ?: ""
            if (weight.isEmpty() || height.isEmpty()) {
                return@setOnClickListener
            }
            onSaveInformationClicked?.invoke(height.toFloat(), weight.toFloat())
            dismiss()
        }
        return dialog
    }

}