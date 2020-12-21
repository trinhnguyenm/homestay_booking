package com.ctr.homestaybooking.data.source.remote.network

import com.ctr.homestaybooking.data.model.BookingStatus
import com.ctr.homestaybooking.data.model.BookingType
import com.ctr.homestaybooking.data.model.CancelType
import com.ctr.homestaybooking.data.model.PlaceStatus
import com.ctr.homestaybooking.data.source.request.*
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

    @PUT("/api/users")
    fun editProfile(@Body userBody: UserBody): Single<UserResponse>

    @PATCH("/api/users/{id}/host")
    fun upToHost(@Path("id") userId: Int): Single<UserResponse>

    /**
     * Place
     * */
    @GET("/api/places")
    fun getPlaces(): Single<PlaceResponse>

    @GET("/api/places/search")
    fun searchPlace(
        @Query("address") address: String?,
        @Query("bookingType") bookingType: BookingType? = null,
        @Query("guestCount") guestCount: Int? = null,
        @Query("roomCount") roomCount: Int? = null,
        @Query("bedCount") bedCount: Int? = null,
        @Query("bathroomCount") bathroomCount: Int? = null,
        @Query("minPrice") minPrice: Double? = null,
        @Query("maxPrice") maxPrice: Double? = null,
        @Query("cancelType") cancelType: CancelType? = null,
        @Query("status") status: PlaceStatus? = PlaceStatus.LISTED,
        @Header("Page-Number") page: Int = 0,
        @Header("Page-Size") size: Int = 20
    ): Single<PlaceResponse>

    @PUT("/api/places/")
    fun editPlace(@Body placeBody: PlaceBody): Single<PlaceDetailResponse>

    @DELETE("/api/places/{id}")
    fun deletePlace(@Path("id") id: Int): Single<PlaceDetailResponse>

    @PATCH("/api/places/{id}/status")
    fun reversePlaceStatusByID(@Path("id") id: Int): Single<PlaceDetailResponse>

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

    @GET("/api/places/{id}/calendar")
    fun getCalendarByPlaceId(@Path("id") id: Int): Single<CalendarResponse>

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

    @POST("/api/reviews")
    fun addReview(@Body reviewBody: ReviewBody): Single<Review>
}
