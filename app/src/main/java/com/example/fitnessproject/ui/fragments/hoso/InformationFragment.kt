package com.example.fitnessproject.ui.fragments.hoso

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.example.fitnessproject.R
import com.example.fitnessproject.ui.BaseFragment
import com.example.fitnessproject.ui.activities.editinformation.EditInformationActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInOptionsExtension
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.fitness.result.DataReadResponse
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.fragment_information.*
import java.util.*
import java.util.concurrent.TimeUnit


class InformationFragment : BaseFragment<InformationViewModel>() {
    override fun getLayoutId() = R.layout.fragment_information
    override fun initScreen() {
        val imgChangeProfile = view?.findViewById<TextView>(R.id.imgChangeProfile)
        val ctlRemider = view?.findViewById<ConstraintLayout>(R.id.ctlRemider)
        imgChangeProfile?.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, EditInformationActivity::class.java)
            startActivity(intent)
        })

        ctlRemider?.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, RemiderActivity::class.java)
            startActivity(intent)
        })

        ctlSync?.setOnClickListener {
            activity?.let { atv ->
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestScopes(Fitness.SCOPE_BODY_READ)
                    .build()
                val mGoogleSignInClient = GoogleSignIn.getClient(atv, gso)
                val signInIntent = mGoogleSignInClient.signInIntent
                startActivityForResult(signInIntent, 1)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 1) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
            getData()
        }

    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            getData()
        } catch (e: ApiException) {
        }
    }

    private fun getData() {
        viewModel.syncGoogleFitData = true
        val fitnessOptions: GoogleSignInOptionsExtension = FitnessOptions.builder()
            .addDataType(DataType.TYPE_WEIGHT, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.TYPE_HEIGHT, FitnessOptions.ACCESS_READ)
            .build()
        activity?.let { atv ->
            val googleSignInAccount = GoogleSignIn.getAccountForExtension(atv, fitnessOptions)

            val response: Task<DataReadResponse> = Fitness.getHistoryClient(
                atv,
                googleSignInAccount
            ).readData(
                DataReadRequest.Builder()
                    .read(DataType.TYPE_WEIGHT)
                    .read(DataType.TYPE_HEIGHT)
                    .setTimeRange(
                        1,
                        Calendar.getInstance().timeInMillis,
                        TimeUnit.MILLISECONDS
                    )
                    .build()
            )
            viewModel.syncGoogleFit(response)
        }
    }

    override fun onResume() {
        super.onResume()
        if (!viewModel.syncGoogleFitData) {
            viewModel.getUserInformation()
        }
    }

    override fun bindData() {
        super.bindData()
        viewModel.userInfoLiveData.observe(viewLifecycleOwner) { user ->
            imgChangeProfile?.text = user.fullName.ifEmpty { "User name" }

            activity?.let {
                user.thumbnail?.let { thumb ->
                    Glide.with(it).load(thumb).into(imgUser)
                }
            }
        }

        viewModel.syncGoogleFitLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(activity, "Đồng bộ thành công", Toast.LENGTH_SHORT).show()
        }
    }
}