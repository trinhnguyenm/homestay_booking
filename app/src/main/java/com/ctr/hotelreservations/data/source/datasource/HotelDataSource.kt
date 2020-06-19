package com.ctr.hotelreservations.data.source.datasource

import com.ctr.hotelreservations.data.source.response.HotelResponse
import io.reactivex.Single

/**
 * Created by at-trinhnguyen2 on 2020/06/19
 */
interface HotelDataSource {
    fun getHotels(): Single<HotelResponse>

}