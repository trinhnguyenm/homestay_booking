package com.ctr.homestaybooking.ui.setupplace

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseActivity
import com.ctr.homestaybooking.data.model.ImageSlideData
import com.ctr.homestaybooking.data.source.PlaceRepository
import com.ctr.homestaybooking.extension.addFragment
import com.ctr.homestaybooking.ui.App

/**
 * Created by at-trinhnguyen2 on 2020/12/03
 */

class PlaceSetupActivity : BaseActivity() {
    internal lateinit var vm: PlaceSetupVMContract

    companion object {
        internal fun start(
            from: Activity,
            placeId: Int = 0
        ) {
            PlaceSetupActivity().apply {
                val intent = Intent(from, PlaceSetupActivity::class.java)
                intent.putExtras(Bundle().apply {
                    putInt(KEY_PLACE_ID, placeId)
                })
                from.startActivity(intent)
            }
        }

        internal const val KEY_PLACE_ID = "key_place_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_setup)
        vm = PlaceSetupViewModel(
            PlaceRepository(),
            App.instance.localRepository
        )
        addFragment(getContainerId(), PlaceSetupOverviewFragment.newInstance())
    }

    override fun getContainerId(): Int = R.id.container

    internal fun openPlaceSetupBasicFragment() {
        addFragment(
            getContainerId(),
            PlaceSetupBasicInfoFragment.newInstance(),
            addToBackStack = true
        )
    }

    internal fun openPlaceSetupImageFragment() {
        addFragment(
            getContainerId(),
            PlaceSetupImageFragment.newInstance(),
            addToBackStack = true
        )
    }

    internal fun openPlaceSetupPrizeFragment() {
        addFragment(
            getContainerId(),
            PlaceSetupPrizeFragment.newInstance(),
            addToBackStack = true
        )
    }

    internal fun openImageSliderFragment(imageSlideData: ImageSlideData) {
        addFragment(
            getContainerId(),
            ImageSliderFragment.newInstance(imageSlideData),
            addToBackStack = true, transactionCallback = {
                it.setCustomAnimations(
                    R.anim.anim_fade_in,
                    0,
                    0,
                    R.anim.anim_fade_out
                )
            }
        )
    }

    internal fun openPlaceSetupCalendarFragment() {
        addFragment(
            getContainerId(),
            PlaceSetupCalendarFragment.newInstance(),
            addToBackStack = true
        )
    }
}
