package com.ctr.homestaybooking.ui.booking

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseActivity
import com.ctr.homestaybooking.data.source.response.MyBookingResponse
import com.ctr.homestaybooking.data.source.response.PromoResponse
import com.ctr.homestaybooking.extension.addFragment

class BookingActivity : BaseActivity() {

    companion object {
        internal const val KEY_PROMO = "key_promos"

        internal fun start(
            from: Fragment,
            startDate: String,
            endDate: String,
            promo: PromoResponse.Promo?
        ) {
            BookingActivity()
//                .apply {
//                    val intent = Intent(from.activity, BookingActivity::class.java)
//                    intent.putExtras(Bundle().apply {
//                        putParcelable(RoomFragment.KEY_BRAND, brand)
//                        putParcelable(PromoDetailActivity.KEY_ROOM_TYPE_STATUS, roomTypeStatus)
//                        putString(PromoDetailActivity.KEY_START_DATE, startDate)
//                        putString(PromoDetailActivity.KEY_END_DATE, endDate)
//                        putParcelable(KEY_PROMO, promo)
//                    })
//                    from.startActivity(intent)
//                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)
        addFragment(R.id.container, BookingFragment.newInstance())
    }

    override fun getContainerId(): Int = R.id.container

    override fun getAppearAnimType(): BaseActivity.AppearAnim =
        BaseActivity.AppearAnim.SLIDE_FROM_RIGHT

    internal fun openPaymentFragment(myBooking: MyBookingResponse.MyBooking) {
        addFragment(
            R.id.container,
            PaymentFragment.newInstance(myBooking),
            addToBackStack = true,
            tag = "BookingFragment.Payment"
        )
    }
}
