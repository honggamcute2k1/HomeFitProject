package com.example.fitnessproject.ui.activities.gender

import android.content.Intent
import android.graphics.Color
import android.widget.Toast
import com.example.fitnessproject.R
import com.example.fitnessproject.domain.model.Gender
import com.example.fitnessproject.ui.BaseActivity
import com.example.fitnessproject.ui.activities.main.MainActivity
import kotlinx.android.synthetic.main.activity_choose_gender.*

class ChooseGenderActivity : BaseActivity<GenderViewModel>() {

    override fun getLayoutId() = R.layout.activity_choose_gender

    private var isMaleSelected = false
    private var isFemaleSelected = false
    override fun initScreen() {
        areaMale?.setOnClickListener {
            isMaleSelected = true
            isFemaleSelected = false
            setupLayoutGenderSelected()
        }

        areaFemale?.setOnClickListener {
            isMaleSelected = false
            isFemaleSelected = true
            setupLayoutGenderSelected()
        }

        btnNext?.setOnClickListener {
            if (!isFemaleSelected && !isMaleSelected) {
                Toast.makeText(this, "Please choose gender", Toast.LENGTH_SHORT).show()
            } else {
                val gender = if (isMaleSelected) Gender.MALE else Gender.FEMALE
                viewModel.chooseGender(gender)
            }
        }
    }

    override fun bindData() {
        super.bindData()
        viewModel.setupUserLiveData.observe(this) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun setupLayoutGenderSelected() {
        if (!isMaleSelected && !isFemaleSelected) {
            txtFemale?.setTextColor(Color.YELLOW)
            txtMale?.setTextColor(Color.YELLOW)
        } else if (isMaleSelected) {
            txtFemale?.setTextColor(Color.WHITE)
            txtMale?.setTextColor(Color.YELLOW)
        } else if (isFemaleSelected) {
            txtFemale?.setTextColor(Color.YELLOW)
            txtMale?.setTextColor(Color.WHITE)
        }
    }
}