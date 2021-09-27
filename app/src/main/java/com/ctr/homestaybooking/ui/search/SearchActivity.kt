package com.ctr.homestaybooking.ui.search

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseActivity
import com.ctr.homestaybooking.data.source.FavoriteRepository
import com.ctr.homestaybooking.data.source.PlaceRepository
import com.ctr.homestaybooking.extension.addFragment
import com.ctr.homestaybooking.ui.App

/**
 * Created by at-trinhnguyen2 on 2020/12/21
 */

class SearchActivity : BaseActivity() {
    internal lateinit var vm: SearchVMContract

    companion object {

        internal fun start(
            from: Fragment
        ) {
            SearchActivity()
                .apply {
                    val intent = Intent(from.activity, SearchActivity::class.java)
                    from.startActivity(intent)
                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        vm = SearchViewModel(
            App.instance.localRepository,
            PlaceRepository(),
            FavoriteRepository()
        )
        addFragment(getContainerId(), SearchFragment.getInstance(), {
            it.setCustomAnimations(
                R.anim.anim_fade_in,
                0,
                0,
                R.anim.anim_fade_out
            )
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        (getCurrentFragment() as? SearchFragment)?.let {
            Log.d("--=", "onBackPressed: ${it}")
            Handler().postDelayed({
                it.searchPlaces()
            }, 400)
        }
    }

    override fun getContainerId(): Int = R.id.container

    override fun getAppearAnimType(): AppearAnim = AppearAnim.FADE_IN

    internal fun openFilterFragment() {
        addFragment(getContainerId(), FilterFragment.getInstance(), addToBackStack = true)
    }
}
