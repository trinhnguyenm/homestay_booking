package com.ctr.hotelreservations.ui.inbox

import com.ctr.hotelreservations.ui.home.HomeContainerBaseFragment

/**
 * Created by at-trinhnguyen2 on 2020/06/02
 */
class InboxContainerFragment : HomeContainerBaseFragment() {

    companion object {
        internal fun getNewInstance() = InboxContainerFragment()
    }

    override fun getRootFragment() = InboxFragment.newInstance()
}
