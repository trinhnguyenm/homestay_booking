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
    private var userResponse: UserResponse? = null

    override fun getUserId() = localRepository.getUserId()

    override fun getUserResponse() = userResponse

    override fun getUserInfo(): Single<UserResponse> {
        return userRepository.getUserById(localRepository.getUserId())
            .addProgressLoading()
            .doOnSuccess {
                userResponse = it
            }
    }

    override fun upToHost(): Single<UserResponse> {
        return userRepository.upToHost(localRepository.getUserId())
            .addProgressLoading()
    }

    override fun isUserSession() = localRepository.isUserSession()

    override fun setHostSession() {
        localRepository.setHostSession()
    }

    override fun setUserSession() {
        localRepository.setUserSession()
    }

    override fun getProgressObservable(): BehaviorSubject<Boolean> =
        progressBarDialogObservable
}
