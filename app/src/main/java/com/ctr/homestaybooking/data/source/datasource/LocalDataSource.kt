package com.ctr.homestaybooking.data.source.datasource

import android.graphics.Bitmap
import android.net.Uri
import java.io.File

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

    fun saveAutoLoginToken(token: String?)

    fun getAutoLoginToken(): String?

    fun saveUserId(id: Int)

    fun getUserId(): Int

    fun removeToken()

    fun removeUserId()

    fun getUUID(): String

    fun isUserSession(): Boolean

    fun setHostSession()

    fun setUserSession()

    fun savePermission(key: String, isChecked: Boolean)

    fun getPermission(key: String): Boolean

    fun handleBitmapToSave(uri: Uri): Bitmap?

    fun createImageFile(): File?

    fun createFileFromBitmap(bitmap: Bitmap): String
}
