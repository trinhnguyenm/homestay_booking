package com.ctr.homestaybooking.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseFragment
import com.ctr.homestaybooking.data.source.UserRepository
import com.ctr.homestaybooking.extension.*
import com.ctr.homestaybooking.ui.App
import kotlinx.android.synthetic.main.fragment_register.*

/**
 * Created by at-trinhnguyen2 on 2020/06/18
 */
class RegisterFragment : BaseFragment() {
    private var vm: LoginVMContract? = null

    companion object {
        fun newInstance() = RegisterFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
        context?.let {
            vm = LoginViewModel(App.instance.localRepository, UserRepository())
        }
    }

    override fun isNeedPaddingTop() = true

    private fun initView() {
        tvTitle.text = "Sign up"
        inputFirstName.visible()
        inputLastName.visible()
        inputPhone.visible()
        inputConfirmPassword.visible()
        tvSignUp.isEnabled = false
    }

    private fun initListener() {
        imgBack.onClickDelayAction {
            activity?.onBackPressed()
        }

        inputFirstName.validateData = {
            it.trim().isNotEmpty()
        }

        inputLastName.validateData = {
            it.trim().isNotEmpty()
        }

        inputPhone.validateData = {
            it.trim().length == 10
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

        inputFirstName.afterTextChange = {
            vm?.getRegisterBody()?.firstName = it
            updateNextButton()
        }

        inputLastName.afterTextChange = {
            vm?.getRegisterBody()?.lastName = it
            updateNextButton()
        }
        inputPhone.afterTextChange = {
            vm?.getRegisterBody()?.phoneNumber = it
            updateNextButton()
        }

        inputEmail.afterTextChange = {
            vm?.getRegisterBody()?.email = it
            updateNextButton()
        }

        inputPassword.afterTextChange = {
            vm?.getRegisterBody()?.password = it
            updateNextButton()
        }

        inputConfirmPassword.afterTextChange = {
            updateNextButton()
        }

        tvActionHere.onClickDelayAction {
            activity?.onBackPressed()
        }

        tvSignUp.onClickDelayAction {
            vm?.let { viewModel ->
                viewModel.register()
                    .observeOnUiThread()
                    .subscribe({
                        if (it.body.id != null) {
                            if (it.body.id > 0) {
                                activity?.showDialog("", "Register successful", "Login Now", {
                                    AuthActivity.start(
                                        this,
                                        isOpenLogin = true,
                                        isShowButtonBack = false,
                                        email = it.body.email
                                    )
                                    activity?.finishAffinity()
                                })
                            }
                        }
                    }, {
                        activity?.showErrorDialog(it)
                    })
            }
        }
    }

    private fun validateData(): Boolean {
        return inputFirstName.isValidateDataNotEmpty() &&
                inputLastName.isValidateDataNotEmpty() &&
                inputPhone.isValidateDataNotEmpty() &&
                inputEmail.isValidateDataNotEmpty() &&
                inputPassword.isValidateDataNotEmpty() &&
                inputConfirmPassword.isValidateDataNotEmpty()
    }

    private fun updateNextButton() {
        tvSignUp.isEnabled = validateData()
    }

    override fun getProgressBarControlObservable() = vm?.getProgressObservable()
}
