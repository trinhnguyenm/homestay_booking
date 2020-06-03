package com.ctr.hotelreservations.ui.home

/**
 * Created by at-trinhnguyen2 on 2020/06/02
 */
class HomeContainerFragment : HomeContainerBaseFragment() {

    companion object {
        internal fun getNewInstance() = HomeContainerFragment()
    }

    override fun getRootFragment() = HomeFragment.getInstance()
}
