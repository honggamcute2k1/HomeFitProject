package com.example.fitnessproject.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.fitnessproject.R
import kotlinx.android.synthetic.main.base_activity.*
import java.lang.reflect.ParameterizedType
import java.util.*

abstract class BaseActivity<V : BaseViewModel> : AppCompatActivity(), CoreInterface {
    lateinit var viewModel: V
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_activity)
        this.layoutInflater.inflate(getLayoutId(), baseView, true)
        Glide.with(this)
            .load(R.drawable.loading)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(imgLoading)
        viewModel = ViewModelProviders
            .of(this)[(this::class.java.genericSuperclass as ParameterizedType)
            .actualTypeArguments[0] as Class<V>]
        viewModel.onCreate()
        initScreen()
        bindData()
    }

    override fun bindData() {
        viewModel.loadingData.observe(this) {
            showLoading(it)
        }
        viewModel.errorLiveData.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isShow: Boolean) {
        frame_loading?.showOrGone(isShow = isShow)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onStart()
    }

    override fun onStop() {
        super.onStop()
    }
}

fun Int.getMonth(): String {
    return when (this) {
        Calendar.JANUARY -> "JANUARY"
        Calendar.FEBRUARY -> "FEBRUARY"
        Calendar.MARCH -> "MARCH"
        Calendar.APRIL -> "APRIL"
        Calendar.MAY -> "MAY"
        Calendar.JUNE -> "JUNE"
        Calendar.JULY -> "JULY"
        Calendar.AUGUST -> "JULY"
        Calendar.SEPTEMBER -> "SEPTEMBER"
        Calendar.OCTOBER -> "OCTOBER"
        Calendar.NOVEMBER -> "NOVEMBER"
        else -> "DECEMBER"
    }
}

fun View.showOrGone(isShow: Boolean) {
    val visibility = if (isShow) View.VISIBLE else View.GONE
    this.visibility = visibility
}