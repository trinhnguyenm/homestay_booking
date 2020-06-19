package com.ctr.hotelreservations.ui.booking

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.base.BaseActivity
import com.ctr.hotelreservations.extension.addFragment
import com.ctr.hotelreservations.ui.roomdetail.RoomDetailActivity

class BookingActivity : BaseActivity() {

    companion object {

        const val REQUEST_CODE_BOOKING = 4351
        const val KEY_ROOM_ID = "key_room_id"
        internal fun start(
            from: Fragment,
            roomId: Int
        ) {
            RoomDetailActivity()
                .apply {
                    val intent = Intent(from.activity, BookingActivity::class.java)
                    intent.putExtra(KEY_ROOM_ID, roomId)
                    from.startActivityForResult(
                        intent,
                        REQUEST_CODE_BOOKING
                    )
                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)
        addFragment(R.id.container, BookingFragment.newInstance())
    }

    override fun getContainerId(): Int = R.id.container

    override fun getAppearAnimType(): BaseActivity.AppearAnim = BaseActivity.AppearAnim.SLIDE_FROM_RIGHT
}