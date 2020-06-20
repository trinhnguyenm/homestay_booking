package com.ctr.hotelreservations.ui.home.room

import com.ctr.hotelreservations.base.BaseViewModel
import com.ctr.hotelreservations.data.source.HotelRepository
import com.ctr.hotelreservations.data.source.response.RoomResponse
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

class RoomViewModel(
    private val hotelRepository: HotelRepository
) : RoomVMContract, BaseViewModel() {

    private val rooms = mutableListOf<RoomResponse.Room>()

    private val roomTypes = mutableListOf<RoomTypeResponse.RoomTypeStatus>()

    override fun getRooms(): MutableList<RoomResponse.Room> = rooms

    override fun getRoomTypes(): MutableList<RoomTypeResponse.RoomTypeStatus> = roomTypes

    override fun getAllRoomByBrand(brandId: Int): Single<RoomResponse> {
        return hotelRepository.getAllRoomByBrand(brandId)
            .addProgressLoading()
            .doOnSuccess { response ->
                getRooms().apply {
                    clear()
                    addAll(response.rooms)
                }
            }
    }

    override fun getAllRoomStatus(
        brandId: Int,
        startDate: String,
        endDate: String
    ): Single<RoomTypeResponse> {
        return hotelRepository.getAllRoomStatus(brandId, startDate, endDate)
            .addProgressLoading()
            .doOnSuccess { response ->
                val a = response
                getRoomTypes().apply {
                    clear()
                    addAll(response.roomTypeStatusList)
                }
            }
    }

    override fun getProgressObservable(): BehaviorSubject<Boolean> =
        progressBarDialogStateObservable
}
