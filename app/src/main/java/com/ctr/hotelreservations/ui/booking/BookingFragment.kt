package com.ctr.hotelreservations.ui.booking

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.base.BaseFragment
import com.ctr.hotelreservations.ui.roomdetail.RoomDetailActivity
import com.ctr.hotelreservations.ui.roomdetail.RoomDetailFragment
import kotlinx.android.synthetic.main.fragment_booking.*
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by at-trinhnguyen2 on 2020/06/16
 */
class BookingFragment : BaseFragment() {
    companion object {
        fun newInstance() = BookingFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(
            "--=",
            "onViewCreated: ${(activity as BookingActivity).intent.getIntExtra(BookingActivity.KEY_ROOM_ID, -1)}"
        )
        initView()
    }

    private fun initView() {
        rcvStepBooking.apply {
            setHasFixedSize(true)
            adapter = BookingStepAdapter(listOf(
                StepBookingUI(StepBooking.STEP_BOOKING,"Booking"),
                StepBookingUI(StepBooking.STEP_BOOKING,"Payment"),
                StepBookingUI(StepBooking.STEP_BOOKING,"Check in"),
                StepBookingUI(StepBooking.STEP_BOOKING,"Review")
            ))
        }
    }
}