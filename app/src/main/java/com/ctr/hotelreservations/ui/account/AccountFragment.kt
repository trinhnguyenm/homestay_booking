package com.ctr.hotelreservations.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.base.BaseFragment

/**
 * Created by at-trinhnguyen2 on 2020/06/02
 */
class AccountFragment : BaseFragment() {

    companion object {
        fun newInstance() = AccountFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }
}