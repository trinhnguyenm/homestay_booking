package com.ctr.homestaybooking.base

import android.net.Uri
import java.io.File

/**
 * @author at-nhatnguyen
 */
interface BaseUploadVMContract {

    fun createImageFile(): File?

    fun handleSaveData(uri: Uri): String

    fun getCurrentPhotoPath(): String

    fun deleteFile(path: String)

    fun savePermission(key: String, isChecked: Boolean)

    fun getPermission(key: String): Boolean
}
