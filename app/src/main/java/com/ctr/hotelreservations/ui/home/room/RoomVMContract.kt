package com.ctr.hotelreservations.ui.home.room

import com.ctr.hotelreservations.data.source.response.RoomResponse
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

interface RoomVMContract {
    fun getProgressObservable(): BehaviorSubject<Boolean>

    fun getRooms(): MutableList<RoomResponse.Room>

    fun getAllRoomByBrand(brandId: Int): Single<RoomResponse>
}
