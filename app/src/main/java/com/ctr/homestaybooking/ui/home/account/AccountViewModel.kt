package com.ctr.homestaybooking.ui.home.account

import com.ctr.homestaybooking.base.BaseViewModel
import com.ctr.homestaybooking.data.source.UserRepository
import com.ctr.homestaybooking.data.source.datasource.LocalDataSource
import com.ctr.homestaybooking.data.source.response.UserResponse
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

class AccountViewModel(
    private val localRepository: LocalDataSource,
    private val userRepository: UserRepository
) : AccountVMContract, BaseViewModel() {

    override fun getUserId() = localRepository.getUserId()

    override fun getUserInfo(): Single<UserResponse> {
        return userRepository.getUserFollowId(localRepository.getUserId())
            .addProgressLoading()
    }

    override fun getProgressObservable(): BehaviorSubject<Boolean> =
        progressBarDialogStateObservable
}
