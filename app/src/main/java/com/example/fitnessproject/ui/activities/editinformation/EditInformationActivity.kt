package com.example.fitnessproject.ui.activities.editinformation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.fitnessproject.R
import com.example.fitnessproject.data.local.entity.User
import com.example.fitnessproject.domain.model.Gender
import com.example.fitnessproject.ui.BaseActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_edit_information.*


class EditInformationActivity : BaseActivity<EditInformationViewModel>() {
    override fun getLayoutId() = R.layout.activity_edit_information

    var imgSelfie: CircleImageView? = null
    var imgBack: ImageView? = null
    var txtAddImageView: TextView? = null
    var uri: Uri? = null

    override fun initScreen() {
        viewModel.getUserInformation()
        imgSelfie = findViewById<CircleImageView>(R.id.imgSelfie)
        val mFragmentManager = supportFragmentManager
        imgBack = findViewById(R.id.imgBack)
        txtAddImageView = findViewById(R.id.txtAddImageview)
        imgBack?.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        btnSave?.setOnClickListener {
            viewModel.user?.fullName = editTextFullName?.text?.toString() ?: ""
            viewModel.user?.born = edtBorn?.text?.toString()?.toInt() ?: 1980
            viewModel.user?.phoneNumber = edtPhone?.text?.toString() ?: ""
            viewModel.user?.thumbnail = uri?.toString()
            viewModel.user?.let { it1 -> viewModel.updateUser(it1) }
        }

        txtAddImageView?.setOnClickListener(View.OnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start()

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            uri = data?.data!!
            imgSelfie?.setImageURI(uri)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    override fun bindData() {
        super.bindData()
        viewModel.userInfoLiveData.observe(this) { user ->
            viewModel.user = user
            setupData(user)
        }

        viewModel.saveUserLiveData.observe(this) {
            finish()
        }
    }

    private fun setupData(user: User) {
        editTextFullName?.setText(user.fullName)
        edtPhone?.setText(user.phoneNumber)
        edtBorn?.setText(user.born.toString())
        editTextGender?.setText(Gender.valueOf(user.gender)?.name ?: "")
        user.thumbnail?.let {
            imgSelfie?.setImageURI(Uri.parse(it))
        }
    }

    override fun onStop() {
        super.onStop()

    }

}