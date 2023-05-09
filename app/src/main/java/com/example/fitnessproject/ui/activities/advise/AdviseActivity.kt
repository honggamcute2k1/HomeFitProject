package com.example.fitnessproject.ui.activities.advise

import com.example.fitnessproject.R
import com.example.fitnessproject.data.network.entity.BmiItem
import com.example.fitnessproject.ui.BaseActivity
import com.example.fitnessproject.ui.BaseViewModel
import kotlinx.android.synthetic.main.activity_advise.*

class AdviseActivity : BaseActivity<BaseViewModel>() {
    companion object {
        const val KEY_BMI_ITEM = "KEY_BMI_ITEM"
    }

    override fun getLayoutId() = R.layout.activity_advise

    override fun initScreen() {
        val bmiItem = intent.getParcelableExtra<BmiItem>(KEY_BMI_ITEM)
        bmiItem?.let { item ->
            val stringBuilder = StringBuilder()
            item.fix?.forEach { f ->
                stringBuilder.append(f).append("\n")
            }
            val content = if (stringBuilder.toString().trim().isNotEmpty()) {
                stringBuilder.toString().trim()
            } else {
                "Bạn đang ở thể trạng tốt"
            }
            txtAdviseDetail?.text = content
        }

        imgBack?.setOnClickListener {
            finish()
        }
    }
}