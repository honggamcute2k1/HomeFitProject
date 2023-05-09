package com.example.fitnessproject.ui.activities.videodetail

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnessproject.R
import com.example.fitnessproject.ui.activities.main.MainActivity

class QuestionExitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_exit)
        val ivBack: ImageView = findViewById(R.id.imgBack)
        val btnExit: Button = findViewById(R.id.mbtnExit)
        val btnSelectOption1: Button = findViewById(R.id.btnSelectOption1)
        val btnSelectOption2: Button = findViewById(R.id.btnSelectOption2)
        val btnSelectOption3: Button = findViewById(R.id.btnSelectOption3)
        val btnSelectOption4: Button = findViewById(R.id.btnSelectOption4)
        ivBack.setOnClickListener {
            onBackPressed()
        }

        btnExit.setOnClickListener {
            goToMain()
        }

        btnSelectOption1.setOnClickListener {
            goToMain()
        }
        btnSelectOption2.setOnClickListener {
            goToMain()
        }
        btnSelectOption3.setOnClickListener {
            goToMain()
        }
        btnSelectOption4.setOnClickListener {
            goToMain()
        }
    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP;
        startActivity(intent)
    }

}
