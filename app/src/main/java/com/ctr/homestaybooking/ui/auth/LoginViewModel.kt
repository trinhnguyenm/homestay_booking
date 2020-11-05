package com.ctr.homestaybooking.ui.auth

import com.ctr.homestaybooking.base.BaseViewModel
import com.ctr.homestaybooking.data.source.datasource.LocalDataSource
import com.ctr.homestaybooking.data.source.datasource.UserDataSource
import com.ctr.homestaybooking.data.source.request.LoginBody
import com.ctr.homestaybooking.data.source.request.UserBody
import com.ctr.homestaybooking.data.source.response.LoginResponse
import com.ctr.homestaybooking.data.source.response.RegisterResponse
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import java.util.*

/**
 * Created by at-trinhnguyen2 on 2020/06/17
 */
class LoginViewModel(
    private val localRepository: LocalDataSource,
    private val userRepository: UserDataSource
) : LoginVMContract, BaseViewModel() {
    private var registerBody =
        UserBody(
            uuid = UUID.randomUUID().toString(),
            deviceToken = (1..100000000).random().toString()
        )

    override fun getRegisterBody() = registerBody

    override fun login(loginBody: LoginBody): Single<LoginResponse> {
        return userRepository.login(loginBody)
            .addProgressLoading()
    }

    override fun register(): Single<RegisterResponse> {
        return userRepository.register(getRegisterBody())
            .addProgressLoading()
    }

    override fun saveAutoLoginToken(token: String?) {
        localRepository.saveAutoLoginToken(token)
    }

    override fun saveUserId(id: Int) {
        localRepository.saveUserId(id)
    }

    override fun getProgressObservable(): BehaviorSubject<Boolean> =
        progressBarDialogStateObservable
}
