package com.ctr.hotelreservations.data.source.remote.network

import com.ctr.hotelreservations.data.source.request.LoginBody
import com.ctr.hotelreservations.data.source.response.HotelResponse
import com.ctr.hotelreservations.data.source.response.LoginResponse
import com.ctr.hotelreservations.data.source.response.RoomResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    /**
     * Sign up new account.
     */
//    @POST("Account/SignUp")
//    fun signUp(@Body signUpBody: SignUpBody): Single<ApiResponse>

    /**
     * Login
     */
    @POST("/api/auth/login")
    fun login(@Body loginBody: LoginBody): Single<LoginResponse>

    @GET("/api/hotels")
    fun getHotels(): Single<HotelResponse>

    @GET("/api/rooms/brand/{id}")
    fun getAllRoomByBrand(@Path("id") brandId: Int): Single<RoomResponse>
}
