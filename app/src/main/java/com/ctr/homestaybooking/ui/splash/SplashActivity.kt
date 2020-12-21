package com.ctr.homestaybooking.ui.splash

import android.content.Intent
import android.os.Bundle
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseActivity
import com.ctr.homestaybooking.base.BaseFragment
import com.ctr.homestaybooking.extension.addFragment
import com.ctr.homestaybooking.extension.getStatusBarHeight
import com.ctr.homestaybooking.ui.auth.AuthActivity
import com.ctr.homestaybooking.ui.home.MyMainActivity
import com.ctr.homestaybooking.ui.onboarding.OnBoardingActivity
import com.ctr.homestaybooking.ui.sheme.SchemeActivity
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
        addFragment(getContainerId(), SplashFragment.newInstance(), {
            it.setCustomAnimations(
                R.anim.anim_fade_in,
                0,
                0,
                R.anim.anim_fade_out
            )
        })
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
        val linkType = intent?.getStringExtra(SchemeActivity.LINK_TYPE)
        val schemeSpecificPart = intent?.getStringExtra(SchemeActivity.SCHEME_SPECIFIC_PART)
        startActivity(
            Intent(this, MyMainActivity::class.java)
                .apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    if (linkType != null) {
                        putExtra(SchemeActivity.LINK_TYPE, linkType)
                    }
                    if (schemeSpecificPart != null) {
                        putExtra(SchemeActivity.SCHEME_SPECIFIC_PART, schemeSpecificPart)
                    }
                }
        )
        finishAffinity()
    }
}
