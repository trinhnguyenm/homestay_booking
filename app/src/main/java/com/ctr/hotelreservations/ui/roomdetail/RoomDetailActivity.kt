package com.ctr.hotelreservations.ui.roomdetail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.base.BaseActivity
import com.ctr.hotelreservations.extension.addFragment

/**
 * Created by at-trinhnguyen2 on 2020/06/13
 */
class RoomDetailActivity : BaseActivity() {

    companion object {
        internal const val KEY_ROOM_ID = "key_room_id"
        private const val REQUEST_CODE_ROOM_DETAIL = 1000
        internal fun start(
            from: Fragment,
            roomId: Int
        ) {
            RoomDetailActivity()
                .apply {
                    val intent = Intent(from.activity, RoomDetailActivity::class.java)
                    intent.putExtra(KEY_ROOM_ID, roomId)
                    from.startActivityForResult(
                        intent,
                        REQUEST_CODE_ROOM_DETAIL
                    )
                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_detail)
        addFragment(getContainerId(), RoomDetailFragment.newInstance())
    }

    override fun getContainerId(): Int = R.id.container

    override fun getAppearAnimType(): AppearAnim = AppearAnim.SLIDE_FROM_RIGHT
}