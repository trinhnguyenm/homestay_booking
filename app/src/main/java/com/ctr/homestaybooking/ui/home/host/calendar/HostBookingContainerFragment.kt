package com.ctr.homestaybooking.ui.home.host.calendar

import com.ctr.homestaybooking.base.HomeContainerBaseFragment

/**
 * Created by at-trinhnguyen2 on 2020/06/02
 */
class HostBookingContainerFragment : HomeContainerBaseFragment() {

    companion object {
        internal fun getNewInstance() =
            HostBookingContainerFragment()
    }

    override fun getRootFragment() =
        HostBookingFragment.newInstance()
}
