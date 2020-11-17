package com.ctr.homestaybooking.data.source.remote.network

import com.ctr.homestaybooking.data.model.BookingStatus
import com.ctr.homestaybooking.data.source.request.BookingBody
import com.ctr.homestaybooking.data.source.request.LoginBody
import com.ctr.homestaybooking.data.source.request.RoomsReservationBody
import com.ctr.homestaybooking.data.source.request.UserBody
import com.ctr.homestaybooking.data.source.response.*
import io.reactivex.Single
import retrofit2.http.*

interface ApiService {

    @POST("/api/auth/login")
    fun login(@Body loginBody: LoginBody): Single<LoginResponse>

    @POST("/api/auth/register")
    fun register(@Body userBody: UserBody): Single<RegisterResponse>

    @GET("/api/users/{id}")
    fun getUserFollowId(@Path("id") userId: Int): Single<UserResponse>

    @GET("/api/places")
    fun getPlaces(): Single<PlaceResponse>

    @GET("/api/places/{id}")
    fun getPlaceDetail(@Path("id") placeId: Int): Single<PlaceDetailResponse>

    @POST("/api/bookings")
    fun addBooking(@Body bookingBody: BookingBody): Single<BookingResponse>

    @GET("/api/rooms/brand/{id}")
    fun getAllRoomByBrand(@Path("id") brandId: Int): Single<RoomResponse>

    @GET("/api/rooms/status/?")
    fun getAllRoomStatus(
        @Query("brandId") brandId: Int,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Single<RoomTypeResponse>

    @GET("api/promos/active")
    fun getAllPromoStillActive(): Single<PromoResponse>

    @POST("/api/room-reservations?")
    fun addNewRoomsReservation(
        @Query("numberOfRooms") numberOfRooms: Int,
        @Query("listPromoCode") listPromoCode: List<String>?,
        @Body roomsReservationBody: RoomsReservationBody
    ): Single<MyBookingResponse>

    @GET("/api/bookings/user/{id}")
    fun getBookingHistory(@Path("id") id: Int): Single<BookingHistoryResponse>

    @PUT("/api/bookings/{id}?")
    fun changeBookingStatus(
        @Path("id") bookingId: Int,
        @Query("bookingStatus") bookingStatus: BookingStatus
    ): Single<BookingResponse>

    @PATCH("/api/room-reservations/{id}/status")
    fun changeRoomReservationStatus(
        @Path("id") roomReservationId: Int,
        @Query("status") status: BookingStatus
    ): Single<ChangeRoomReservationStatusResponse>
}
