package com.ctr.hotelreservations.data.source.datasource

/**
 * This interface used to declare the functions which use to get data from local storage.
 */
interface LocalDataSource {

    /**
     * Check app run first time
     */
    fun isFirstLaunch(): Boolean

    /**
     * update value the first time launch
     */
    fun updateFirstLaunch()

    /**
     * get device token
     */
    fun getDeviceToken(): String?

    /**
     * get uuid.
     */
    fun getUUID(): String
}