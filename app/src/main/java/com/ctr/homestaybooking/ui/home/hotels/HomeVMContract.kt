package com.ctr.homestaybooking.ui.home.hotels

import com.ctr.homestaybooking.data.source.response.HotelResponse
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

interface HomeVMContract {
    fun getProgressObservable(): BehaviorSubject<Boolean>

    fun getHotelList(): MutableList<HotelResponse.Hotel>

    fun getHotels(): Single<HotelResponse>
}
