package com.ctr.homestaybooking.ui.home.host.place

import com.ctr.homestaybooking.base.HomeContainerBaseFragment

/**
 * Created by at-trinhnguyen2 on 2020/06/02
 */
class HostPlaceContainerFragment : HomeContainerBaseFragment() {

    companion object {
        internal fun getNewInstance() =
            HostPlaceContainerFragment()
    }

    override fun getRootFragment() = HostPlaceFragment.getInstance()
}
