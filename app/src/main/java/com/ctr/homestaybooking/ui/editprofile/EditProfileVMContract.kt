package com.ctr.homestaybooking.ui.editprofile

import com.ctr.homestaybooking.data.source.request.UserBody
import com.ctr.homestaybooking.data.source.response.UserDetail
import com.ctr.homestaybooking.data.source.response.UserResponse
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by at-trinhnguyen2 on 2020/06/17
 */
interface EditProfileVMContract {

    fun getProgressObservable(): BehaviorSubject<Boolean>
    fun getUserBody(): UserBody
    fun getUserInfo(): Single<UserResponse>
    fun getUserDetail(): UserDetail?
    fun editProfile(): Single<UserResponse>
}
