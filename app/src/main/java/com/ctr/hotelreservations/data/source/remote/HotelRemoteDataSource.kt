package com.ctr.hotelreservations.data.source.remote

import com.ctr.hotelreservations.data.source.datasource.HotelDataSource
import com.ctr.hotelreservations.data.source.remote.network.ApiClient
import com.ctr.hotelreservations.data.source.remote.network.ApiService
import com.ctr.hotelreservations.data.source.request.RoomsReservationBody
import com.ctr.hotelreservations.data.source.response.RoomReservationResponse
import io.reactivex.Single

/**
 * Created by at-trinhnguyen2 on 2020/06/19
 */
class HotelRemoteDataSource(private val apiService: ApiService = ApiClient.getInstance(null).service) :
    HotelDataSource {
    override fun getHotels() = apiService.getHotels()

    override fun getAllRoomByBrand(brandId: Int) = apiService.getAllRoomByBrand(brandId)

    override fun getAllRoomStatus(brandId: Int, startDate: String, endDate: String) =
        apiService.getAllRoomStatus(brandId, startDate, endDate)

    override fun addNewRoomsReservation(
        numberOfRooms: Int,
        listPromoCode: List<String>?,
        roomsReservationBody: RoomsReservationBody
    ): Single<RoomReservationResponse> {
        apiService.getHotels()
        return apiService.addNewRoomsReservation(numberOfRooms, listPromoCode, roomsReservationBody)
    }
}
