package com.example.fitnessproject.ui.activities.splash

import android.content.Intent
import android.widget.Toast
import com.example.fitnessproject.R
import com.example.fitnessproject.ui.BaseActivity
import com.example.fitnessproject.ui.activities.gender.ChooseGenderActivity
import com.example.fitnessproject.ui.activities.main.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity<SplashViewModel>() {
    override fun getLayoutId() = R.layout.activity_splash
    override fun initScreen() {
        viewModel.initDataBaseFirstTime()

    }

    override fun bindData() {
        super.bindData()
        viewModel.initSuccessLiveData.observe(this) {
            if (it) {
                if (viewModel.isSetupFirstTime()) {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                } else {
                    startActivity(Intent(this@SplashActivity, ChooseGenderActivity::class.java))
                }
                finish()
            } else {
                Toast.makeText(this, "Error init database", Toast.LENGTH_SHORT).show()
            }
        }
    }
}