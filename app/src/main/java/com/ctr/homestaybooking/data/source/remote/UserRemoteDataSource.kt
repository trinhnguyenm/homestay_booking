package com.ctr.homestaybooking.data.source.remote

import com.ctr.homestaybooking.data.source.datasource.UserDataSource
import com.ctr.homestaybooking.data.source.remote.network.ApiClient
import com.ctr.homestaybooking.data.source.remote.network.ApiService
import com.ctr.homestaybooking.data.source.request.LoginBody
import com.ctr.homestaybooking.data.source.request.UserBody

/**
 * Created by at-trinhnguyen2 on 2020/06/17
 */
class UserRemoteDataSource(private val apiService: ApiService = ApiClient.getInstance(null).service) :
    UserDataSource {

    override fun login(body: LoginBody) = apiService.login(body)

    override fun register(body: UserBody) = apiService.register(body)

    override fun getUserFollowId(userId: Int) = apiService.getUserFollowId(userId)

    override fun upToHost(userId: Int) = apiService.upToHost(userId)
}
