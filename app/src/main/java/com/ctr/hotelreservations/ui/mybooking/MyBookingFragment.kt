package com.ctr.hotelreservations.ui.mybooking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.base.BaseFragment

/**
 * Created by at-trinhnguyen2 on 2020/06/02
 */
class MyBookingFragment : BaseFragment() {

    companion object {
        fun newInstance() = MyBookingFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_booking, container, false)
    }
}
