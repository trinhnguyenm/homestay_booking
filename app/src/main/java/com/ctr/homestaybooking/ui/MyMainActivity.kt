package com.ctr.homestaybooking.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.ui.home.HomeActivity
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

    @LayoutRes
    override fun getLayout(): Int {
        return R.layout.activity_my_view_pager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            R.drawable.bg_icon_tab_save,
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
}
