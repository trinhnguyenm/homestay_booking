package com.ctr.homestaybooking.ui.auth

import com.ctr.homestaybooking.data.source.request.LoginBody
import com.ctr.homestaybooking.data.source.request.UserBody
import com.ctr.homestaybooking.data.source.response.LoginResponse
import com.ctr.homestaybooking.data.source.response.RegisterResponse
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by at-trinhnguyen2 on 2020/06/17
 */
interface LoginVMContract {

    fun login(loginBody: LoginBody): Single<LoginResponse>

    fun getProgressObservable(): BehaviorSubject<Boolean>

    fun saveAutoLoginToken(token: String?)

    fun saveUserId(id: Int)

    fun register(): Single<RegisterResponse>

    fun getRegisterBody(): UserBody
}
