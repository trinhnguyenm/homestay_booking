package com.ctr.homestaybooking.ui.home.host.progress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseFragment

/**
 * Created by at-trinhnguyen2 on 2020/06/02
 */
class ProgressFragment : BaseFragment() {

    companion object {
        fun getNewInstance() =
            ProgressFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_progress, container, false)
    }
}
