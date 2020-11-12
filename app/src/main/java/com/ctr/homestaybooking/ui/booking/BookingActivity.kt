package com.ctr.homestaybooking.ui.booking

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseActivity
import com.ctr.homestaybooking.data.source.response.Booking
import com.ctr.homestaybooking.data.source.response.PlaceDetailResponse
import com.ctr.homestaybooking.extension.addFragment
import com.ctr.homestaybooking.ui.placedetail.PlaceDetailActivity.Companion.KEY_END_DATE
import com.ctr.homestaybooking.ui.placedetail.PlaceDetailActivity.Companion.KEY_PLACE_DETAIL
import com.ctr.homestaybooking.ui.placedetail.PlaceDetailActivity.Companion.KEY_START_DATE

class BookingActivity : BaseActivity() {

    companion object {

        internal fun start(
            from: Activity,
            placeDetailResponse: PlaceDetailResponse,
            startDate: String,
            endDate: String
        ) {
            BookingActivity().apply {
                val intent = Intent(from, BookingActivity::class.java)
                intent.putExtras(Bundle().apply {
                    putParcelable(KEY_PLACE_DETAIL, placeDetailResponse)
                    putString(KEY_START_DATE, startDate)
                    putString(KEY_END_DATE, endDate)
                })
                from.startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)
        addFragment(R.id.container, BookingFragment.newInstance())
    }

    override fun getContainerId(): Int = R.id.container

    override fun getAppearAnimType(): AppearAnim =
        AppearAnim.SLIDE_FROM_RIGHT

    internal fun openPaymentFragment(booking: Booking) {
        addFragment(
            R.id.container,
            PaymentFragment.newInstance(booking),
            addToBackStack = true,
            tag = "BookingFragment.Payment"
        )
    }
}
