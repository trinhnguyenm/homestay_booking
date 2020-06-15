package com.ctr.hotelreservations.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.base.BaseFragment
import com.ctr.hotelreservations.data.model.RoomInfo
import com.ctr.hotelreservations.ui.roomdetail.RoomDetailActivity
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by at-trinhnguyen2 on 2020/05/31
 */
class HomeFragment : BaseFragment() {

    companion object {
        fun getInstance() = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val homeAdapter = HomeAdapter(
            listOf(
                RoomInfo(1, "", "", "", true, "title 1", 1, 1f, 1),
                RoomInfo(2, "", "", "", true, "title 2", 1, 1f, 1),
                RoomInfo(3, "", "", "", true, "title 3", 1, 1f, 1),
                RoomInfo(4, "", "", "", true, "title 4", 1, 1f, 1),
                RoomInfo(5, "", "", "", true, "title 5", 1, 1f, 1),
                RoomInfo(6, "", "", "", true, "title 6", 1, 1f, 1)
            )
        )
        rcvHome.apply {
            setHasFixedSize(true)
            adapter = homeAdapter
        }

        homeAdapter.onItemClicked = {
            RoomDetailActivity.start(this, it.roomId)
        }
    }
}
