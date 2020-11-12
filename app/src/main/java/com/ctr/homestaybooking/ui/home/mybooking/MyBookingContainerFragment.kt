package com.ctr.homestaybooking.ui.home.mybooking

import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.HomeContainerBaseFragment
import com.ctr.homestaybooking.data.source.response.Booking
import com.ctr.homestaybooking.extension.addFragment
import com.ctr.homestaybooking.ui.booking.PaymentFragment

/**
 * Created by at-trinhnguyen2 on 2020/06/02
 */
class MyBookingContainerFragment : HomeContainerBaseFragment() {

    companion object {
        internal fun getNewInstance() =
            MyBookingContainerFragment()
    }

    override fun getRootFragment() =
        MyBookingFragment.newInstance()

    internal fun openPaymentFragment(booking: Booking) {
        addFragment(getContainerId(), PaymentFragment.newInstance(booking), {
            it.setCustomAnimations(
                R.anim.anim_slide_right_in,
                0,
                0,
                R.anim.anim_slide_right_out
            )
        }, true)
    }
}
