package com.example.fitnessproject.ui.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.LinearLayout
import com.example.fitnessproject.R
import kotlinx.android.synthetic.main.layout_menu_bottom.view.*

class BottomMenu @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var onActionClicked: ((MenuType) -> Unit)? = null

    init {
        inflate(context, R.layout.layout_menu_bottom, this)
        initAction()
    }

    private fun initAction() {
        actionMenu1?.setOnClickListener {
            setupLayout(MenuType.TAP_LUYEN)
            onActionClicked?.invoke(MenuType.TAP_LUYEN)
        }
        actionMenu2?.setOnClickListener {
            setupLayout(MenuType.BAO_CAO)
            onActionClicked?.invoke(MenuType.BAO_CAO)
        }
        actionMenu3?.setOnClickListener {
            setupLayout(MenuType.HO_SO)
            onActionClicked?.invoke(MenuType.HO_SO)
        }
    }

    private fun resetLayout() {
        txtPractice?.setTextColor(Color.WHITE)
        txtReport?.setTextColor(Color.WHITE)
        txtUser?.setTextColor(Color.WHITE)
    }

    fun setupLayout(menuType: MenuType) {
        resetLayout()
        when (menuType) {
            MenuType.TAP_LUYEN -> {
                txtPractice.setTextColor(Color.CYAN)
            }
            MenuType.HO_SO -> {
                txtUser.setTextColor(Color.CYAN)

            }
            MenuType.BAO_CAO -> {
                txtReport.setTextColor(Color.CYAN)
            }
        }
    }
}

enum class MenuType {
    TAP_LUYEN,
    BAO_CAO,
    HO_SO
}