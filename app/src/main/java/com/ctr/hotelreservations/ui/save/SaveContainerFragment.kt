package com.ctr.hotelreservations.ui.save

import com.ctr.hotelreservations.ui.home.HomeContainerBaseFragment
import com.ctr.hotelreservations.ui.home.HomeContainerFragment
import com.ctr.hotelreservations.ui.home.HomeFragment

/**
 * Created by at-trinhnguyen2 on 2020/06/02
 */
class SaveContainerFragment : HomeContainerBaseFragment() {

    companion object {
        internal fun getNewInstance() = SaveContainerFragment()
    }

    override fun getRootFragment() = SaveFragment.newInstance()
}
