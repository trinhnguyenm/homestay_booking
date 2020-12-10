package com.ctr.homestaybooking.data.source

import com.ctr.homestaybooking.data.source.datasource.UserDataSource
import com.ctr.homestaybooking.data.source.remote.UserRemoteDataSource
import com.ctr.homestaybooking.data.source.request.LoginBody
import com.ctr.homestaybooking.data.source.request.UserBody

/**
 * Created by at-trinhnguyen2 on 2020/06/17
 */
class UserRepository : UserDataSource {

    private val userRemoteDataSource = UserRemoteDataSource()

    override fun login(body: LoginBody) = userRemoteDataSource.login(body)

    override fun register(body: UserBody) = userRemoteDataSource.register(body)

    override fun editProfile(body: UserBody) = userRemoteDataSource.editProfile(body)

    override fun getUserById(userId: Int) = userRemoteDataSource.getUserById(userId)

    override fun upToHost(userId: Int) = userRemoteDataSource.upToHost(userId)
}
