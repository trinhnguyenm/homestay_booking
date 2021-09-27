package com.ctr.homestaybooking.ui.placedetail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseActivity
import com.ctr.homestaybooking.data.source.response.PlaceDetail
import com.ctr.homestaybooking.extension.onClickDelayAction
import com.ctr.homestaybooking.util.toCalendar
import kotlinx.android.synthetic.main.fragment_review.*

/**
 * Created by at-trinhnguyen2 on 2020/12/21
 */


class ReviewListActivity : BaseActivity() {

    companion object {
        internal fun start(
            from: Fragment,
            placeDetail: PlaceDetail
        ) {
            ReviewListActivity()
                .apply {
                    val intent = Intent(from.activity, ReviewListActivity::class.java)
                    intent.putExtra(KEY_PLACE_DETAIL, placeDetail)
                    from.startActivity(intent)
                }
        }

        internal const val KEY_PLACE_DETAIL = "key_place_detail"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_review)
        initRecyclerView()
        initListener()
    }

    override fun getContainerId(): Int = R.id.container

    private fun initListener() {
        ivBack.onClickDelayAction {
            onBackPressed()
        }
    }

    private fun initRecyclerView() {
        recyclerView.let {
            it.setHasFixedSize(true)
            it.adapter = ReviewAdapter(
                intent?.getParcelableExtra<PlaceDetail>(KEY_PLACE_DETAIL)?.reviews
                    ?.sortedByDescending { review -> review.createDate.toCalendar().time }
                    ?: listOf()
            )
        }
    }
}
