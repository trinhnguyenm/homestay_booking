package com.ctr.homestaybooking.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.SystemClock
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.data.source.LocalRepository
import com.ctr.homestaybooking.extension.hideKeyboard
import com.ctr.homestaybooking.ui.home.MyMainActivity
import io.reactivex.plugins.RxJavaPlugins
import sdk.chat.app.firebase.ChatSDKFirebase
import sdk.chat.core.session.ChatSDK
import sdk.chat.ui.module.UIModule

/**
 * Created by at-trinhnguyen2 on 2020/05/31
 */
class App : Application(), Application.ActivityLifecycleCallbacks {

    companion object {
        @SuppressLint("StaticFieldLeak")
        internal lateinit var instance: App
            private set
    }

    internal lateinit var localRepository: LocalRepository
    internal var currentActivity: Activity? = null
    internal var isIgnoreAppGoToBackground = false
    private var currentActivityCount = 0
    private var isAppGoToBackground = false
    private var gotoBackgroundTime = 0L

    override fun onCreate() {
        super.onCreate()
        initChatSDK()
        localRepository = LocalRepository(applicationContext)
        instance = this
        registerActivityLifecycleCallbacks(this)
        // Add here to prevent crash app in case when Rx error make crash app
        RxJavaPlugins.setErrorHandler {}
    }

    override fun onActivityStopped(p0: Activity?) {
        currentActivityCount--
        if (isIgnoreAppGoToBackground) {
            isIgnoreAppGoToBackground = false
            return
        }
        if (currentActivityCount == 0) {
            isAppGoToBackground = true
            gotoBackgroundTime = SystemClock.elapsedRealtime()
        }
    }

    override fun onActivityPaused(p0: Activity?) {
        p0?.currentFocus?.clearFocus()
        p0?.hideKeyboard()
    }

    override fun onActivityStarted(activity: Activity?) {

    }

    override fun onActivityResumed(p0: Activity?) {
        currentActivity = p0
    }

    override fun onActivityDestroyed(p0: Activity?) = Unit

    override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) = Unit

    override fun onActivityCreated(p0: Activity?, p1: Bundle?) = Unit

    private fun initChatSDK() {
        ChatSDKFirebase.quickStart(
            this,
            "pre_1",
            "",
            false
        )
        UIModule.config().apply {
            publicRoomRoomsEnabled = false
            setTheme(R.style.Theme_AppCompat_Light_NoActionBar_ChatSDKTheme)
        }
        ChatSDK.config().logoDrawableResourceID = R.mipmap.ic_launcher
        ChatSDK.builder()
            .setPushNotificationColor(R.color.colorPrimary)
            .setPushNotificationImageDefaultResourceId(R.mipmap.ic_launcher)
        ChatSDK.ui().mainActivity = MyMainActivity::class.java
//        ChatSDK.ui().chatActivity = MyChatActivity::class.java
//        ChatSDK.ui().setPrivateThreadsFragment(MyPrivateThreadsFragment.getInstance())
    }
}
