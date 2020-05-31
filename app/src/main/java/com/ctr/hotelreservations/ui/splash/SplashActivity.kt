package com.ctr.hotelreservations.ui.splash

import android.content.Intent
import android.os.Bundle
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.base.BaseActivity
import com.ctr.hotelreservations.extension.getStatusBarHeight
import com.ctr.hotelreservations.extension.replaceFragment
import com.ctr.hotelreservations.ui.HomeActivity
import com.ctr.hotelreservations.util.SharedReferencesUtil
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * Created by at-trinhnguyen2 on 2020/05/31
 */
class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if (getStatusBarHeight() == 0) {
            val containerPosition = IntArray(2)
            container.getLocationOnScreen(containerPosition)
            if (containerPosition.last() > 0) {
                SharedReferencesUtil.saveInt(
                    this,
                    SharedReferencesUtil.KEY_STATUS_BAR_HEIGHT,
                    containerPosition.last()
                )
                processOnCreate()
                return
            }
            container.viewTreeObserver.addOnGlobalLayoutListener {
                if (containerPosition.last() == 0) {
                    container.getLocationOnScreen(containerPosition)
                    SharedReferencesUtil.saveInt(
                        this,
                        SharedReferencesUtil.KEY_STATUS_BAR_HEIGHT,
                        containerPosition.last()
                    )
                    processOnCreate()
                }
            }
        } else {
            processOnCreate()
        }
    }

    override fun getContainerId() = R.id.container

    private fun processOnCreate() {
        fullScreenActivity()
        openSplashFragment()
    }

    private fun openSplashFragment() {
        replaceFragment(getContainerId(), SplashFragment.newInstance())
    }

    internal fun startHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
        finishAffinity()
    }
}