package com.ctr.homestaybooking.ui.splash

import android.content.Intent
import android.os.Bundle
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseActivity
import com.ctr.homestaybooking.base.BaseFragment
import com.ctr.homestaybooking.extension.getStatusBarHeight
import com.ctr.homestaybooking.extension.replaceFragment
import com.ctr.homestaybooking.ui.auth.AuthActivity
import com.ctr.homestaybooking.ui.home.MainActivity
import com.ctr.homestaybooking.ui.onboarding.OnBoardingActivity
import com.ctr.homestaybooking.util.SharedReferencesUtil

/**
 * Created by at-trinhnguyen2 on 2020/05/31
 */
class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        SharedReferencesUtil.setInt(
            this,
            SharedReferencesUtil.KEY_STATUS_BAR_HEIGHT,
            this@SplashActivity.getStatusBarHeight()
        )
        processOnCreate()
    }

    override fun getContainerId() = R.id.container

    private fun processOnCreate() {
        fullScreenActivity()
        openSplashFragment()
    }

    private fun openSplashFragment() {
        replaceFragment(getContainerId(), SplashFragment.newInstance())
    }

    internal fun startOnBoardingActivity() {
        startActivity(Intent(this, OnBoardingActivity::class.java))
        finishAffinity()
    }

    internal fun startAuthActivity(fragment: BaseFragment) {
        AuthActivity.start(fragment, isOpenLogin = true, isShowButtonBack = false)
        finishAffinity()
    }

    internal fun startHomeActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finishAffinity()
    }
}
