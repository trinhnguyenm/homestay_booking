package com.ctr.homestaybooking.ui.splash

/**
 * Created by at-trinhnguyen2 on 2020/05/31
 */
interface SplashVMContract {

    fun isFirstLunch(): Boolean

    fun updateFirstLaunch()

    fun getLoginToken(): String?
    fun getUserId(): Int
}
