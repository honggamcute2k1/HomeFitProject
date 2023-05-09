package com.example.fitnessproject.ui.dialog


import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.fitnessproject.R

class DialogCongratulation : DialogFragment() {

    companion object {
        const val NUMBER = "NUMBER"
        fun getInstance(number: Int): DialogCongratulation {
            val dialog = DialogCongratulation()
            val bundle = Bundle()
            bundle.putInt(NUMBER, number)
            dialog.arguments = bundle
            return dialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setContentView(R.layout.layout_dialog_congratulations)
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
        arguments?.get(NUMBER)?.let {
            dialog.findViewById<TextView>(R.id.iv)?.text =
                getString(R.string.content, it.toString())
        }
        return dialog
    }
}
