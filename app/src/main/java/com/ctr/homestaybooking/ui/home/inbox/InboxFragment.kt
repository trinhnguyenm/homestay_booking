package com.ctr.homestaybooking.ui.home.inbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseFragment

/**
 * Created by at-trinhnguyen2 on 2020/11/17
 */
class InboxFragment : BaseFragment() {

    companion object {
        fun newInstance() = InboxFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inbox, container, false)
    }

}
