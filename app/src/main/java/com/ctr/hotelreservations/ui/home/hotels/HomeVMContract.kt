package com.ctr.hotelreservations.ui.home.hotels

import com.ctr.hotelreservations.data.source.response.HotelResponse
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

interface HomeVMContract {
    fun getProgressObservable(): BehaviorSubject<Boolean>

    fun getHotelList(): MutableList<HotelResponse.Body>

    fun getHotels(): Single<HotelResponse>
}