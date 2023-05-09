package com.example.fitnessproject.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.fitnessproject.R
import kotlinx.android.synthetic.main.base_fragment.*
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<V : BaseViewModel> : Fragment(), CoreInterface {
    lateinit var viewModel: V

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.base_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        viewModel.loadingData.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        viewModel.errorLiveData.observe(viewLifecycleOwner) {

        }
    }

    private fun showLoading(isShow: Boolean) {
        frame_loading?.showOrGone(isShow = isShow)
    }

}