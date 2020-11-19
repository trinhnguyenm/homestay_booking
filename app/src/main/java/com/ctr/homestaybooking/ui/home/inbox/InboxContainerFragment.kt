package com.ctr.homestaybooking.ui.home.inbox

import com.ctr.homestaybooking.base.HomeContainerBaseFragment

/**
 * Created by at-trinhnguyen2 on 2020/06/02
 */
class InboxContainerFragment : HomeContainerBaseFragment() {

    companion object {
        internal fun getNewInstance() = InboxContainerFragment()
    }

    override fun getRootFragment() = InboxFragment.newInstance()
}
