package com.ctr.homestaybooking.ui.auth

import android.os.Bundle
import android.util.Log
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
import com.ctr.homestaybooking.ui.home.MyMainActivity
import kotlinx.android.synthetic.main.fragment_login.*
import sdk.chat.core.session.ChatSDK
import sdk.chat.core.types.AccountDetails
import sdk.guru.common.RX

/**
 * Created by at-trinhnguyen2 on 2020/06/16
 */
class LoginFragment : BaseFragment() {
    private lateinit var vm: LoginVMContract
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
            vm = LoginViewModel(App.instance.localRepository, UserRepository())
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
            vm.getLoginBody().email = it
            updateButtonNext()
        }

        inputPassword.afterTextChange = {
            vm.getLoginBody().password = it
            updateButtonNext()
        }

        tvLogin.onClickDelayAction {
            vm.login(vm.getLoginBody())
                .observeOnUiThread()
                .subscribe({
                    val details =
                        AccountDetails.username(
                            vm.getLoginBody().email,
                            vm.getLoginBody().password
                        )
                    ChatSDK.auth().authenticate(details)
                        .observeOn(RX.main())
                        .doOnSubscribe {
                            getProgressObservable().onNext(true)
                        }
                        .doFinally {
                            getProgressObservable().onNext(false)
                        }
                        .subscribe(
                            {
                                vm.saveAutoLoginToken(it.authToken.token)
                                vm.saveUserId(it.authToken.userDetail.id)
                                activity?.let { it1 -> MyMainActivity.start(it1) }
                                activity?.finishAffinity()
                            },
                            { activity?.showErrorDialog(it.apply { Log.d("--=", "+${this}") }) }
                        )
                }, {
                    activity?.showErrorDialog(it)
                })
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

    override fun getProgressObservable() = vm.getProgressObservable()
}
