package com.ctr.homestaybooking.ui.splash

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseFragment
import com.ctr.homestaybooking.data.source.LocalRepository

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
        viewModel = context?.let { SplashViewModel(LocalRepository(it)) }

        Handler().postDelayed({
            viewModel?.let {
                if (it.getLoginToken().isNullOrEmpty()) {
                    (activity as? SplashActivity)?.startAuthActivity(this)
                } else {
                    (activity as? SplashActivity)?.startHomeActivity()
                }
            }
        }, 300L)
    }
}
