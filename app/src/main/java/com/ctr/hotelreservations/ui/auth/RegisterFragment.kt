package com.ctr.hotelreservations.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.base.BaseFragment
import com.ctr.hotelreservations.data.source.UserRepository
import com.ctr.hotelreservations.data.source.request.LoginBody
import com.ctr.hotelreservations.extension.*
import com.ctr.hotelreservations.ui.App
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * Created by at-trinhnguyen2 on 2020/06/18
 */
class RegisterFragment : BaseFragment() {
    private var viewModel: LoginVMContract? = null
    private var loginBody = LoginBody()

    companion object {
        fun newInstance() = RegisterFragment()
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
        initView()
        initListener()
        context?.let {
            viewModel = LoginViewModel(App.instance.localRepository, UserRepository())
        }
    }

    override fun isNeedPaddingTop() = true

    private fun initView() {
        tvTitle.text = "Sign up"
        tvLogin.text = "Sign up"
        inputFirstName.visible()
        inputLastName.visible()
        inputPhone.visible()
        inputConfirmPassword.visible()
    }

    private fun initListener() {
        inputFirstName.validateData = {
            it.trim().isNotEmpty()
        }

        inputLastName.validateData = {
            it.trim().isNotEmpty()
        }

        inputPhone.validateData = {
            it.trim().isNotEmpty() && it.trim().length <= 10
        }

        inputEmail.validateData = {
            it.trim().isNotEmpty() && it.isEmailValid()
        }

        inputPassword.validateData = {
            it.trim().isNotEmpty() && it.isPasswordValid()
        }

        inputConfirmPassword.validateData = {
            it.trim().isNotEmpty() && it == inputPassword.getText()
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
                        Log.d("--=", "initListener: ${it}")
                    }, {
                        Log.d("--=", "initListener: ${it}")
                        activity?.showErrorDialog(it)
                    })
            }
        }
    }

    override fun getProgressBarControlObservable() = viewModel?.getProgressObservable()
}