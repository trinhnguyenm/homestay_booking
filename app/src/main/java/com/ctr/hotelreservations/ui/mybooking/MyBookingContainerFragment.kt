package com.ctr.hotelreservations.ui.mybooking

import com.ctr.hotelreservations.ui.home.HomeContainerBaseFragment
import com.ctr.hotelreservations.ui.home.HomeContainerFragment
import com.ctr.hotelreservations.ui.home.HomeFragment

/**
 * Created by at-trinhnguyen2 on 2020/06/02
 */
class MyBookingContainerFragment : HomeContainerBaseFragment() {

    companion object {
        internal fun getNewInstance() = MyBookingContainerFragment()
    }

    override fun getRootFragment() = MyBookingFragment.newInstance()
}
