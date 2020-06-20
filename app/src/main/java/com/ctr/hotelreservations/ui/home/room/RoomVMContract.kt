package com.ctr.hotelreservations.ui.home.room

import com.ctr.hotelreservations.data.source.response.RoomResponse
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

interface RoomVMContract {
    fun getProgressObservable(): BehaviorSubject<Boolean>

    fun getRooms(): MutableList<RoomResponse.Room>

    fun getRoomTypes(): MutableList<RoomTypeResponse.RoomTypeStatus>

    fun getAllRoomByBrand(brandId: Int): Single<RoomResponse>

    fun getAllRoomStatus(brandId: Int, startDate: String, endDate: String): Single<RoomTypeResponse>

}
