package com.example.fitnessproject.ui.activities.main

import androidx.fragment.app.FragmentManager
import com.example.fitnessproject.R
import com.example.fitnessproject.ui.BaseActivity
import com.example.fitnessproject.ui.BaseFragment
import com.example.fitnessproject.ui.dialog.DialogCongratulation
import com.example.fitnessproject.ui.fragments.baocao.ReportFragment
import com.example.fitnessproject.ui.fragments.hoso.InformationFragment
import com.example.fitnessproject.ui.fragments.tapluyen.PracticeFragment
import com.example.fitnessproject.ui.view.MenuType
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainViewModel>() {
    override fun getLayoutId() = R.layout.activity_main

    override fun initScreen() {
        switchMenu(MenuType.TAP_LUYEN)
        bottomMenu?.setupLayout(MenuType.TAP_LUYEN)
        bottomMenu?.onActionClicked = { menuType ->
            switchMenu(menuType)
        }
    }

    override fun bindData() {
        super.bindData()
        viewModel.dayLiveData.observe(this) {
            DialogCongratulation.getInstance(it).show(supportFragmentManager, "")
        }
    }

    private fun switchMenu(menuType: MenuType) {
        when (menuType) {
            MenuType.TAP_LUYEN -> {
                switchFragment(PracticeFragment::class.java)
            }
            MenuType.BAO_CAO -> {
                switchFragment(ReportFragment::class.java)
            }
            MenuType.HO_SO -> {
                switchFragment(InformationFragment::class.java)
            }
        }
    }


    private fun switchFragment(fragment: Class<out BaseFragment<*>>) {
        val fragmentCurrent = supportFragmentManager.findFragmentById(R.id.frameContent)
        // pop all fragments in backstack when click menu
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        try {
            val newFragment = fragment.newInstance();
            var currentSimpleName = ""
            if (fragmentCurrent != null) {
                currentSimpleName = fragmentCurrent.javaClass.simpleName;
            }
            if (currentSimpleName != newFragment.javaClass.simpleName) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frameContent, newFragment).commit();
            }
        } catch (e: Exception) {
        }
    }
}