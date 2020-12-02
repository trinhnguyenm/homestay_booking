package com.ctr.homestaybooking.ui.home.rooms

import com.ctr.homestaybooking.data.source.response.RoomResponse
import com.ctr.homestaybooking.data.source.response.RoomTypeResponse
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

interface RoomVMContract {
    fun getProgressObservable(): BehaviorSubject<Boolean>

    fun getRooms(): MutableList<RoomResponse.Room>

    fun getRoomTypes(): MutableList<RoomTypeResponse.RoomTypeStatus>

    fun getAllRoomStatus(
        brandId: Int,
        startDate: String,
        endDate: String,
        numOfGuest: Int,
        numOfRoom: Int
    ): Single<RoomTypeResponse>

    fun filterRoomStatus(numOfGuest: Int, numOfRoom: Int)
}
