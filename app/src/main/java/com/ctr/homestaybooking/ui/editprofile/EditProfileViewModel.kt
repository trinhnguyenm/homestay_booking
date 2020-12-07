package com.ctr.homestaybooking.ui.editprofile

import com.ctr.homestaybooking.base.BaseViewModel
import com.ctr.homestaybooking.data.source.datasource.LocalDataSource
import com.ctr.homestaybooking.data.source.datasource.UserDataSource
import com.ctr.homestaybooking.data.source.request.UserBody
import com.ctr.homestaybooking.data.source.response.UserDetail
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by at-trinhnguyen2 on 2020/06/17
 */
class EditProfileViewModel(
    private val localRepository: LocalDataSource,
    private val userRepository: UserDataSource
) : EditProfileVMContract, BaseViewModel() {
    private var registerBody =
        UserBody(
            deviceToken = localRepository.getDeviceToken()
        )

    private var userDetail: UserDetail? = null

    override fun getUserBody() = registerBody

    override fun getUserDetail() = userDetail

    override fun getUserInfo() =
        userRepository.getUserById(localRepository.getUserId()).addProgressLoading().doOnSuccess {
            userDetail = it.userDetail
            registerBody = it.userDetail.toRegisterBody()
        }

    override fun editProfile() = userRepository.editProfile(getUserBody())
        .addProgressLoading()

    override fun getProgressObservable(): BehaviorSubject<Boolean> =
        progressBarDialogObservable
}
