package com.ctr.homestaybooking.ui.home.inbox

import com.ctr.homestaybooking.R
import sdk.chat.ui.fragments.PrivateThreadsFragment

/**
 * Created by at-trinhnguyen2 on 2020/11/25
 */
class MyPrivateThreadsFragment : PrivateThreadsFragment() {
    override fun getLayout() = R.layout.fragment_my_threads

    companion object {
        internal fun getInstance(): MyPrivateThreadsFragment =
            MyPrivateThreadsFragment.getInstance()
    }
}
