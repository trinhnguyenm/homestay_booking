package com.ctr.hotelreservations.data.source.datasource

import com.ctr.hotelreservations.data.source.request.LoginBody
import com.ctr.hotelreservations.data.source.response.LoginResponse
import io.reactivex.Single

/**
 * Created by at-trinhnguyen2 on 2020/06/17
 */
interface UserDataSource {
    fun login(body: LoginBody): Single<LoginResponse>

}