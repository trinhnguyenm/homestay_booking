package com.ctr.homestaybooking.ui.home.places

import com.ctr.homestaybooking.base.HomeContainerBaseFragment

/**
 * Created by at-trinhnguyen2 on 2020/06/02
 */
class HomeContainerFragment : HomeContainerBaseFragment() {

    companion object {
        internal fun getNewInstance() =
            HomeContainerFragment()
    }

    override fun getRootFragment() = HomeFragment.getInstance()
}
