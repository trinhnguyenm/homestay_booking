package com.ctr.homestaybooking.ui.auth

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseChooseImageFragment
import com.ctr.homestaybooking.data.source.UserRepository
import com.ctr.homestaybooking.extension.*
import com.ctr.homestaybooking.ui.App
import com.ctr.homestaybooking.ui.home.MyMainActivity
import kotlinx.android.synthetic.main.fragment_register.*
import sdk.chat.core.session.ChatSDK
import sdk.chat.core.types.AccountDetails
import sdk.guru.common.RX
import java.io.File

/**
 * Created by at-trinhnguyen2 on 2020/06/18
 */
class RegisterFragment : BaseChooseImageFragment() {
    private lateinit var vm: LoginVMContract


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

    override fun getImageView(): ImageView = imgAvatar

    override fun getPathImageSuccess(path: String) {
        context?.uploadImageFirebase(listOf(Uri.fromFile(File(path)))) { task ->
            if (task.isSuccessful) {
                vm.getRegisterBody().imageUrl = task.result.toString()
            } else {
                Log.d("--=", "getPathImageSuccess: ${task.exception}")
                task.exception?.let {
                    activity?.showErrorDialog(it)
                }
            }
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
            vm.getRegisterBody().firstName = it
            updateNextButton()
        }

        inputLastName.afterTextChange = {
            vm.getRegisterBody().lastName = it
            updateNextButton()
        }
        inputPhone.afterTextChange = {
            vm.getRegisterBody().phoneNumber = it
            updateNextButton()
        }

        inputEmail.afterTextChange = {
            vm.getRegisterBody().email = it
            updateNextButton()
        }

        inputPassword.afterTextChange = {
            vm.getRegisterBody().password = it
            updateNextButton()
        }

        inputConfirmPassword.afterTextChange = {
            updateNextButton()
        }

        tvActionHere.onClickDelayAction {
            activity?.onBackPressed()
        }

        imgAvatar.onClickDelayAction {
            showPopupGetImage()
        }

        tvSignUp.onClickDelayAction {
            ChatSDK.auth().authenticate(
                AccountDetails.signUp(
                    vm.getRegisterBody().email,
                    vm.getRegisterBody().password
                )
            )
                .doOnSubscribe {
                    getProgressObservable().onNext(true)
                }
                .doFinally {
                    getProgressObservable().onNext(false)
                }.observeOn(RX.main())
                .subscribe({
                    vm.getRegisterBody().apply {
                        uuid = ChatSDK.auth().currentUserEntityID
                    }
                    ChatSDK.currentUser().name =
                        vm.getRegisterBody().firstName + " " + vm.getRegisterBody().lastName
                    ChatSDK.currentUser().update()
                    ChatSDK.core().pushUser()
                        .observeOnUiThread()
                        .doOnSubscribe { vm.getProgressObservable().onNext(true) }
                        .doFinally { vm.getProgressObservable().onNext(false) }
                        .subscribe({
                        }, {
                            activity?.showErrorDialog(it)
                        })
                    vm.let { viewModel ->
                        viewModel.register()
                            .observeOnUiThread()
                            .subscribe({
                                if (it.authToken.userDetail.id > 0) {
                                    activity?.showDialog(
                                        "",
                                        "Đăng kí thành công",
                                        getString(R.string.ok),
                                        {
                                            vm.saveAutoLoginToken(it.authToken.token)
                                            vm.saveUserId(it.authToken.userDetail.id)
                                            activity?.let { it1 -> MyMainActivity.start(it1) }
                                            activity?.finishAffinity()
                                        })
                                }
                            }, {
                                activity?.showErrorDialog(it)
                            })
                    }
                }, {
                    activity?.showErrorDialog(it)
                })
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

    override fun getProgressObservable() = vm.getProgressObservable()
}
