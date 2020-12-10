package com.ctr.homestaybooking.base

import android.net.Uri
import com.ctr.homestaybooking.data.source.datasource.LocalDataSource
import java.io.File
import java.io.IOException

/**
 * @author at-nhatnguyen
 */
open class BaseUploadViewModel(private var localRepository: LocalDataSource) :
    BaseUploadVMContract {

    private var currentPhotoPath = ""

    override fun createImageFile(): File? {
        var image: File? = null
        try {
            image = localRepository.createImageFile()
        } catch (e: IOException) {
            //no op
        }
        image?.also {
            currentPhotoPath = it.absolutePath
        }
        return image
    }

    override fun handleSaveData(uri: Uri): String {
        var pathImage = ""
        localRepository.handleBitmapToSave(uri)?.let {
            pathImage = localRepository.createFileFromBitmap(it)
        }
        return pathImage
    }

    override fun getCurrentPhotoPath() = currentPhotoPath

    override fun deleteFile(path: String) {
        val fDelete = File(path)
        if (fDelete.exists()) {
            fDelete.delete()
        }
    }

    override fun savePermission(key: String, isChecked: Boolean) {
        localRepository.savePermission(key, isChecked)
    }

    override fun getPermission(key: String): Boolean = localRepository.getPermission(key)
}
