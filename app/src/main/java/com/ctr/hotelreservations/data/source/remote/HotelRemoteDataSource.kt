package com.ctr.hotelreservations.data.source.remote

import com.ctr.hotelreservations.data.source.datasource.HotelDataSource
import com.ctr.hotelreservations.data.source.remote.network.ApiClient
import com.ctr.hotelreservations.data.source.remote.network.ApiService

/**
 * Created by at-trinhnguyen2 on 2020/06/19
 */
class HotelRemoteDataSource(private val apiService: ApiService = ApiClient.getInstance(null).service) :
    HotelDataSource {
    override fun getHotels() = apiService.getHotels()

    override fun getAllRoomByBrand(brandId: Int) = apiService.getAllRoomByBrand(brandId)
}
