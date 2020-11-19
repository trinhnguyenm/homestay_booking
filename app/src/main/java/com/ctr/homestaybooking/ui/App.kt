package com.ctr.homestaybooking.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.data.source.LocalRepository
import com.ctr.homestaybooking.extension.hideKeyboard
import com.ctr.homestaybooking.ui.home.account.AccountContainerFragment
import com.ctr.homestaybooking.ui.home.favotite.FavoriteContainerFragment
import com.ctr.homestaybooking.ui.home.mybooking.MyBookingContainerFragment
import com.ctr.homestaybooking.ui.home.places.HomeContainerFragment
import io.reactivex.plugins.RxJavaPlugins
import sdk.chat.app.firebase.ChatSDKFirebase
import sdk.chat.core.session.ChatSDK
import sdk.chat.core.types.AccountDetails
import sdk.chat.ui.module.UIModule
import sdk.guru.common.RX

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
            setTheme(R.style.Theme_AppCompat_Light_NoActionBar_FullScreen)
        }
        ChatSDK.currentUser()
        if (ChatSDK.auth().isAuthenticatedThisSession) {
            // do nothing
            Log.d("--=", "authenticated}")
        } else if (ChatSDK.auth().isAuthenticated || ChatSDK.auth().isAuthenticating) {
            ChatSDK.auth().authenticate()
                .observeOn(RX.main())
                .subscribe({},
                    { Log.i("--=", "authenticate error $it") }
                )
        } else {
            val details = AccountDetails.username("congtrinhnguyen56@gmail.com", "12121212")
            ChatSDK.auth().authenticate(details)
                .observeOn(RX.main())
                .subscribe(
                    {
                        Log.i("--=", "authenticate ok ${ChatSDK.auth().isAuthenticated}")
                    },
                    { Log.i("--=", "authenticate error $it") }
                )
        }

        // If you are overriding the ChatActivity, you will also need to define the main activity for your app
        ChatSDK.ui().mainActivity = MyMainActivity::class.java
        ChatSDK.ui().removeTab(0)
        ChatSDK.ui().removeTab(0)
        ChatSDK.ui().apply {
            setTab(
                "Home",
                getDrawable(R.drawable.bg_icon_tab_home),
                HomeContainerFragment.getNewInstance(),
                0
            )
            setTab(
                "Save",
                getDrawable(R.drawable.bg_icon_tab_save),
                FavoriteContainerFragment.getNewInstance(),
                1
            )
            setTab(
                "My Booking",
                getDrawable(R.drawable.bg_icon_tab_my_booking),
                MyBookingContainerFragment.getNewInstance(),
                2
            )
            setTab(
                "Account",
                getDrawable(R.drawable.bg_icon_tab_account),
                AccountContainerFragment.getNewInstance(),
                4
            )
            setTab(privateThreadsTab().apply {
                title = "Inbox"
                icon = getDrawable(R.drawable.ic_tab_inbox)
            }, 3)
        }
    }
}
