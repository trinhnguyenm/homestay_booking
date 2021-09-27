package com.ctr.homestaybooking.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.ui.App
import com.ctr.homestaybooking.ui.home.account.AccountContainerFragment
import com.ctr.homestaybooking.ui.home.favotite.FavoriteContainerFragment
import com.ctr.homestaybooking.ui.home.host.calendar.HostBookingContainerFragment
import com.ctr.homestaybooking.ui.home.host.place.HostPlaceContainerFragment
import com.ctr.homestaybooking.ui.home.host.progress.ProgressFragment
import com.ctr.homestaybooking.ui.home.mybooking.MyBookingContainerFragment
import com.ctr.homestaybooking.ui.home.places.HomeContainerFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.miguelcatalan.materialsearchview.MaterialSearchView
import kotlinx.android.synthetic.main.activity_my_view_pager.*
import sdk.chat.core.Tab
import sdk.chat.core.dao.Thread
import sdk.chat.core.session.ChatSDK
import sdk.chat.ui.activities.MainActivity
import sdk.chat.ui.adapters.PagerAdapterTabs
import sdk.chat.ui.fragments.BaseFragment
import sdk.chat.ui.icons.Icons
import sdk.chat.ui.interfaces.SearchSupported

/**
 * Created by at-trinhnguyen2 on
 */
open class MyMainActivity : MainActivity() {
    protected var adapter: PagerAdapterTabs? = null
    private lateinit var vm: HomeVMContract

    companion object {
        internal fun start(from: Activity) {
            MyMainActivity().apply {
                from.startActivity(Intent(from, MyMainActivity::class.java))
            }
        }
    }

    @LayoutRes
    override fun getLayout(): Int {
        return R.layout.activity_my_view_pager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = HomeViewModel(App.instance.localRepository)
        initSdk()
        initViews()
        initTab()
    }

    override fun searchEnabled(): Boolean {
        return currentTab()?.fragment is SearchSupported
    }

    override fun search(text: String?) {
        val fragment = currentTab()?.fragment
        if (fragment is SearchSupported) {
            (fragment as SearchSupported).filter(text)
        }
    }

    override fun searchView(): MaterialSearchView? {
        return searchView
    }

    override fun initViews() {
        super.initViews()

        // Only creates the adapter if it wasn't initiated already
        if (adapter == null) {
            adapter = PagerAdapterTabs(supportFragmentManager)
        }
        val tabs = adapter?.tabs
        if (tabs != null) {
            for (tab in tabs) {
                tabLayout.addTab(tabLayout.newTab().setText(tab.title))
            }
        }
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tabSelected(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        viewPager.offscreenPageLimit = 5
        val tab = tabLayout.getTabAt(0)
        tab?.let { tabSelected(it) }
    }

    public override fun onStart() {
        super.onStart()
        if (supportActionBar != null) {
            supportActionBar?.setHomeAsUpIndicator(
                Icons.get(
                    this,
                    Icons.choose().user,
                    Icons.shared().actionBarIconColor
                )
            )
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun tabSelected(tab: TabLayout.Tab) {
        val index = tab.position
        viewPager.currentItem = index
        val tabs = adapter?.tabs

//        Fragment currentFragment = adapter.getTabs().get(index).fragment;
//        if (getSupportActionBar() != null) {
//            if (currentFragment instanceof HasAppbar) {
//                getSupportActionBar().hide();
//            } else {
//                getSupportActionBar().show();
//            }
//        }
        updateLocalNotificationsForTab()

        // We mark the tab as visible. This lets us be more efficient with updates
        // because we only
        tabs?.forEachIndexed { index, it ->
            val fragment = it?.fragment
            if (fragment is BaseFragment) {
                (it.fragment as BaseFragment).setTabVisibility(index == tab.position)
            }
        }

        searchView.closeSearch()
    }

    private fun currentTab(): Tab? {
        return adapter?.tabs?.get(viewPager.currentItem)
    }

    private fun initTab() {
        val icons = mutableListOf(
            R.drawable.bg_icon_tab_home,
            if (App.instance.localRepository.isUserSession()) R.drawable.bg_icon_tab_save else R.drawable.bg_icon_tab_progress,
            R.drawable.bg_icon_tab_my_booking,
            R.drawable.bg_icon_tab_inbox,
            R.drawable.bg_icon_tab_account
        )
        for (i in 0 until HomeActivity.PAGER_NUMBER) {
            val tab =
                View.inflate(this, R.layout.item_tab_layout, null) as? ConstraintLayout
            (tab?.findViewById(R.id.ivIconTab) as? ImageView)?.setImageResource(icons[i])
            (tab?.findViewById(R.id.tvTitleTab) as? TextView)?.text =
                viewPager.adapter?.getPageTitle(i)
            tabLayout.getTabAt(i)?.customView = tab
        }
    }

    override fun updateLocalNotificationsForTab() {
        adapter?.tabs?.get(tabLayout.selectedTabPosition)?.let {
            ChatSDK.ui().setLocalNotificationHandler { thread: Thread? ->
                showLocalNotificationsForTab(
                    it.fragment,
                    thread
                )
            }
        }
    }

    override fun clearData() {
        adapter?.tabs?.forEach {
            if (it.fragment is BaseFragment) {
                (it.fragment as BaseFragment).clearData()
            }
        }
    }

    override fun reloadData() {
        adapter?.tabs?.forEach {
            if (it.fragment is BaseFragment) {
                (it.fragment as BaseFragment).safeReloadData()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                ChatSDK.ui().startProfileActivity(this, ChatSDK.currentUserID())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initSdk() {
        ChatSDK.ui().removeTab(0)
        ChatSDK.ui().removeTab(0)
        if (App.instance.localRepository.isUserSession()) {
            ChatSDK.ui().apply {
                setTab(
                    "Khám phá",
                    getDrawable(R.drawable.bg_icon_tab_home),
                    HomeContainerFragment.getNewInstance(),
                    0
                )
                setTab(
                    "Ưa thích",
                    getDrawable(R.drawable.bg_icon_tab_save),
                    FavoriteContainerFragment.getNewInstance(),
                    1
                )
                setTab(
                    "Đặt chỗ của tôi",
                    getDrawable(R.drawable.bg_icon_tab_my_booking),
                    MyBookingContainerFragment.getNewInstance(),
                    2
                )
                setTab(
                    "Tài khoản",
                    getDrawable(R.drawable.bg_icon_tab_account),
                    AccountContainerFragment.getNewInstance(),
                    4
                )
                setTab(privateThreadsTab().apply {
                    title = "Tin nhắn"
                    icon = getDrawable(R.drawable.ic_tab_inbox)
                }, 3)
            }
        } else {
            ChatSDK.ui().apply {
                setTab(
                    "Chỗ ở",
                    getDrawable(R.drawable.bg_icon_tab_save),
                    HostPlaceContainerFragment.getNewInstance(),
                    0
                )
                setTab(
                    "Thống kê",
                    getDrawable(R.drawable.bg_icon_tab_my_booking),
                    ProgressFragment.getNewInstance(),
                    1
                )
                setTab(
                    "Quản lý đơn đặt chỗ",
                    getDrawable(R.drawable.bg_icon_tab_home),
                    HostBookingContainerFragment.getNewInstance(),
                    2
                )
                setTab(
                    "Tài khoản",
                    getDrawable(R.drawable.bg_icon_tab_account),
                    AccountContainerFragment.getNewInstance(),
                    4
                )
                setTab(privateThreadsTab().apply {
                    title = "Tin nhắn"
                    icon = getDrawable(R.drawable.ic_tab_inbox)
                }, 3)
            }
        }
    }

    internal fun setTabSelection(position: Int) {
        tabLayout.getTabAt(position)?.select()
    }

    internal fun getFragmentInsideViewPager(position: Int = viewPager.currentItem) =
        viewPager.adapter?.instantiateItem(
            viewPager,
            position
        ) as? com.ctr.homestaybooking.base.BaseFragment
}
