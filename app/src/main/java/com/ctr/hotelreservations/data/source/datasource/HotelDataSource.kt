package com.ctr.hotelreservations.data.source.datasource

import com.ctr.hotelreservations.data.source.response.HotelResponse
import com.ctr.hotelreservations.data.source.response.RoomResponse
import com.ctr.hotelreservations.ui.home.room.RoomTypeResponse
import io.reactivex.Single

/**
 * Created by at-trinhnguyen2 on 2020/06/19
 */
interface HotelDataSource {
    fun getHotels(): Single<HotelResponse>

    fun getAllRoomByBrand(brandId: Int): Single<RoomResponse>

    fun getAllRoomStatus(brandId: Int, startDate: String, endDate: String): Single<RoomTypeResponse>
}
