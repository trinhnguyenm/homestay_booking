package com.ctr.homestaybooking.data.source.remote

import com.ctr.homestaybooking.data.model.BookingStatus
import com.ctr.homestaybooking.data.source.datasource.PlaceDataSource
import com.ctr.homestaybooking.data.source.remote.network.ApiClient
import com.ctr.homestaybooking.data.source.remote.network.ApiService
import com.ctr.homestaybooking.data.source.request.RoomsReservationBody
import com.ctr.homestaybooking.data.source.response.MyBookingResponse
import io.reactivex.Single

/**
 * Created by at-trinhnguyen2 on 2020/06/19
 */
class PlaceRemoteDataSource(private val apiService: ApiService = ApiClient.getInstance(null).service) :
    PlaceDataSource {
    override fun getPlaces() = apiService.getPlaces()

    override fun getAllRoomByBrand(brandId: Int) = apiService.getAllRoomByBrand(brandId)

    override fun getAllRoomStatus(brandId: Int, startDate: String, endDate: String) =
        apiService.getAllRoomStatus(brandId, startDate, endDate)

    override fun addNewRoomsReservation(
        numberOfRooms: Int,
        listPromoCode: List<String>?,
        roomsReservationBody: RoomsReservationBody
    ): Single<MyBookingResponse> {
        apiService.getPlaces()
        return apiService.addNewRoomsReservation(numberOfRooms, listPromoCode, roomsReservationBody)
    }

    override fun getAllPromoStillActive() = apiService.getAllPromoStillActive()

    override fun getBookingHistory() = apiService.getBookingHistory()

    override fun changeReservationStatus(reservationId: Int) =
        apiService.changeReservationStatus(reservationId)

    override fun changeRoomReservationStatus(roomReservationId: Int) =
        apiService.changeRoomReservationStatus(roomReservationId, BookingStatus.CANCELLED)
}
