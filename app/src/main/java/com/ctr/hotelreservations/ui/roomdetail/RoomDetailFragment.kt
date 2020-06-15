package com.ctr.hotelreservations.ui.roomdetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.base.BaseFragment
import com.ctr.hotelreservations.ui.roomdetail.RoomDetailActivity.Companion.KEY_ROOM_ID

/**
 * Created by at-trinhnguyen2 on 2020/06/11
 */
class RoomDetailFragment : BaseFragment() {
    companion object {
        fun newInstance() = RoomDetailFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_room_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(
            "--=",
            "onViewCreated: ${(activity as RoomDetailActivity).intent.getIntExtra(KEY_ROOM_ID, -1)}"
        )
    }
}