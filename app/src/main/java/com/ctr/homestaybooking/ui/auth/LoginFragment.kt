package com.ctr.homestaybooking.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseFragment
import com.ctr.homestaybooking.data.source.UserRepository
import com.ctr.homestaybooking.data.source.request.LoginBody
import com.ctr.homestaybooking.extension.*
import com.ctr.homestaybooking.ui.App
import com.ctr.homestaybooking.ui.auth.AuthActivity.Companion.KEY_EMAIL
import com.ctr.homestaybooking.ui.auth.AuthActivity.Companion.KEY_SHOW_BUTTON_BACK
import com.ctr.homestaybooking.ui.home.MainActivity
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
        initView()
        initListener()
        context?.let {
            viewModel = LoginViewModel(App.instance.localRepository, UserRepository())
        }
        inputEmail.setText(activity?.intent?.extras?.getString(KEY_EMAIL))
    }

    private fun initView() {
        if (activity?.intent?.extras?.getBoolean(KEY_SHOW_BUTTON_BACK) != false) {
            imgBack.visible()
        } else {
            imgBack.invisible()
        }
        tvLogin.isEnabled = false
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
            it.trim().isNotEmpty()
        }

        inputEmail.afterTextChange = {
            loginBody.email = it
            updateButtonNext()
        }

        inputPassword.afterTextChange = {
            loginBody.password = it
            updateButtonNext()
        }

        tvLogin.onClickDelayAction {
            viewModel?.let { viewModel ->
                viewModel.login(loginBody)
                    .observeOnUiThread()
                    .subscribe({
                        viewModel.saveAutoLoginToken(it.authToken.token)
                        viewModel.saveUserId(it.authToken.userDetail.id)
                        activity?.let { it1 -> MainActivity.start(it1) }
                        activity?.finishAffinity()
                    }, {
                        activity?.showErrorDialog(it)
                    })
            }
        }

        tvActionHere.onClickDelayAction {
            (activity as? AuthActivity)?.openRegisterFragment()
        }
    }

    private fun updateButtonNext() {
        tvLogin.isEnabled = validateData()
    }

    private fun validateData(): Boolean {
        return inputEmail.isValidateDataNotEmpty()
                && inputPassword.isValidateDataNotEmpty()
    }

    override fun getProgressBarControlObservable() = viewModel?.getProgressObservable()
}
