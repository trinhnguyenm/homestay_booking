package com.ctr.homestaybooking.ui.roomdetail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseActivity
import com.ctr.homestaybooking.extension.addFragment

/**
 * Created by at-trinhnguyen2 on 2020/06/13
 */
class PlaceDetailActivity : BaseActivity() {

    companion object {
        internal const val KEY_ROOM_TYPE_STATUS = "key_room_type_status"
        internal const val KEY_PLACE_ID = "key_place_id"
        internal const val KEY_START_DATE = "key_start_date"
        internal const val KEY_END_DATE = "key_end_date"

        internal fun start(
            from: Fragment,
            placeId: Int,
            startDate: String? = null,
            endDate: String? = null
        ) {
            PlaceDetailActivity()
                .apply {
                    val intent = Intent(from.activity, PlaceDetailActivity::class.java)
                    intent.putExtras(Bundle().apply {
                        putInt(KEY_PLACE_ID, placeId)
                        putString(KEY_START_DATE, startDate)
                        putString(KEY_END_DATE, endDate)
                    })
                    from.startActivity(intent)
                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)
        addFragment(getContainerId(), PlaceDetailFragment.newInstance())
    }

    override fun getContainerId(): Int = R.id.container

    override fun getAppearAnimType(): AppearAnim = AppearAnim.SLIDE_FROM_RIGHT
}
