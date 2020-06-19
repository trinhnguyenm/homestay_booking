package com.ctr.hotelreservations.ui.mybooking

import com.ctr.hotelreservations.base.HomeContainerBaseFragment

/**
 * Created by at-trinhnguyen2 on 2020/06/02
 */
class MyBookingContainerFragment : HomeContainerBaseFragment() {

    companion object {
        internal fun getNewInstance() = MyBookingContainerFragment()
    }

    override fun getRootFragment() = MyBookingFragment.newInstance()
}
