package com.ctr.hotelreservations.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.base.BaseFragment
import com.ctr.hotelreservations.data.source.UserRepository
import com.ctr.hotelreservations.data.source.request.LoginBody
import com.ctr.hotelreservations.extension.*
import com.ctr.hotelreservations.ui.App
import com.ctr.hotelreservations.ui.home.MainActivity
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * Created by at-trinhnguyen2 on 2020/06/16
 */
class LoginFragment : BaseFragment() {
    private var viewModel: LoginVMContract? = null
    private var loginBody = LoginBody()

    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        context?.let {
            viewModel = LoginViewModel(App.instance.localRepository, UserRepository())
        }
    }

    override fun isNeedPaddingTop() = true

    private fun initListener() {
        imgBack.onClickDelayAction {
            activity?.onBackPressed()
        }

        inputEmail.validateData = {
            it.trim().isNotEmpty() && it.isEmailValid()
        }

        inputPassword.validateData = {
            it.trim().isNotEmpty() && it.isPasswordValid()
        }

        inputEmail.afterTextChange = {
            loginBody.email = it
        }

        inputPassword.afterTextChange = {
            loginBody.password = it
        }

        tvLogin.onClickDelayAction {
            viewModel?.let { viewModel ->
                viewModel.login(loginBody)
                    .observeOnUiThread()
                    .subscribe({
                        viewModel.saveAutoLoginToken(it.body.token)
                        activity?.let { it1 -> MainActivity.start(it1) }
                    }, {
                        activity?.showErrorDialog(it)
                    })
            }
        }
    }

    override fun getProgressBarControlObservable() = viewModel?.getProgressObservable()
}