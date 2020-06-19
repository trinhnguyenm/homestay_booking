package com.ctr.hotelreservations.data.source

import com.ctr.hotelreservations.data.source.datasource.HotelDataSource
import com.ctr.hotelreservations.data.source.remote.HotelRemoteDataSource

/**
 * Created by at-trinhnguyen2 on 2020/06/19
 */
class HotelRepository : HotelDataSource {

    private val hotelRemoteDataSource = HotelRemoteDataSource()

    override fun getHotels() = hotelRemoteDataSource.getHotels()

    override fun getAllRoomByBrand(brandId: Int) = hotelRemoteDataSource.getAllRoomByBrand(brandId)
}
