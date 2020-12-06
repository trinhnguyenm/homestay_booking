package com.ctr.homestaybooking.data.source.remote.network

import com.ctr.homestaybooking.data.model.BookingStatus
import com.ctr.homestaybooking.data.source.request.BookingBody
import com.ctr.homestaybooking.data.source.request.LoginBody
import com.ctr.homestaybooking.data.source.request.PlaceBody
import com.ctr.homestaybooking.data.source.request.UserBody
import com.ctr.homestaybooking.data.source.response.*
import io.reactivex.Single
import retrofit2.http.*

interface ApiService {
    /**
     * Auth
     * */
    @POST("/api/auth/login")
    fun login(@Body loginBody: LoginBody): Single<LoginResponse>

    @POST("/api/auth/register")
    fun register(@Body userBody: UserBody): Single<LoginResponse>

    /**
     * User
     * */
    @GET("/api/users/{id}")
    fun getUserFollowId(@Path("id") userId: Int): Single<UserResponse>

    @PATCH("/api/users/{id}/host")
    fun upToHost(@Path("id") userId: Int): Single<UserResponse>

    /**
     * Place
     * */
    @GET("/api/places")
    fun getPlaces(): Single<PlaceResponse>

    @PUT("/api/places/")
    fun editPlace(@Body placeBody: PlaceBody): Single<PlaceDetailResponse>

    @GET("/api/places/host/{id}")
    fun getPlacesByHostId(@Path("id") id: Int): Single<HostPlaceResponse>

    @GET("/api/places/{id}")
    fun getPlaceDetail(@Path("id") placeId: Int): Single<PlaceDetailResponse>

    @GET("/api/placeTypes")
    fun getPlaceTypes(): Single<PlaceTypeResponse>

    @GET("/api/provinces")
    fun getProvinces(): Single<ProvinceResponse>

    @GET("/api/provinces/{id}")
    fun getProvinceById(@Path("id") id: Int): Single<ProvinceDetailResponse>

    @GET("/api/rooms/status/?")
    fun getAllRoomStatus(
        @Query("brandId") brandId: Int,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Single<RoomTypeResponse>

    /**
     * Booking
     * */
    @POST("/api/bookings")
    fun addBooking(@Body bookingBody: BookingBody): Single<BookingResponse>

    @GET("/api/bookings/{id}")
    fun getBookingById(@Path("id") id: Int): Single<BookingResponse>

    @GET("/api/bookings/user/{id}")
    fun getBookingHistory(@Path("id") id: Int): Single<BookingHistoryResponse>

    @GET("/api/bookings/host/{id}")
    fun getBookingByHostId(@Path("id") id: Int): Single<BookingHistoryResponse>

    @PATCH("/api/bookings/{id}?")
    fun changeBookingStatus(
        @Path("id") bookingId: Int,
        @Query("bookingStatus") bookingStatus: BookingStatus
    ): Single<BookingResponse>

    @POST("/api/bookings/{id}/payment")
    fun requestPayment(
        @Path("id") bookingId: Int
    ): Single<CaptureMoMoApiResponse>
}
