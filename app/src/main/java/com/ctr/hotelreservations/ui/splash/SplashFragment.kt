package com.ctr.hotelreservations.ui.splash

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.base.BaseFragment

/**
 * Created by at-trinhnguyen2 on 2020/05/31
 */
class SplashFragment : BaseFragment() {
    private var viewModel: SplashVMContract? = null

    companion object {
        fun newInstance() = SplashFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler().postDelayed({ navigation() }, 1000L)
    }

    override fun isNeedPaddingTop() = false

    private fun navigation() {
        (activity as? SplashActivity)?.startHomeActivity()
    }
}