package com.ctr.homestaybooking.data.source

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.provider.MediaStore
import androidx.exifinterface.media.ExifInterface
import com.ctr.homestaybooking.data.source.datasource.LocalDataSource
import com.ctr.homestaybooking.util.ImageUtil
import com.ctr.homestaybooking.util.SharedReferencesUtil
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * This class used to define the functions which use to get data from local storage.
 */
class LocalRepository(private val context: Context) : LocalDataSource {

    companion object {
        private const val TAG = "LocalRepository"
    }

    private val charset = StandardCharsets.UTF_8

    override fun isFirstLaunch(): Boolean =
        SharedReferencesUtil.getBoolean(context, SharedReferencesUtil.KEY_IS_FIRST_LAUNCH, true)

    override fun updateFirstLaunch() {
        SharedReferencesUtil.setBoolean(context, SharedReferencesUtil.KEY_IS_FIRST_LAUNCH, false)
    }

    override fun saveAutoLoginToken(token: String?) {
        SharedReferencesUtil.setString(
            context,
            SharedReferencesUtil.KEY_AUTO_LOGIN_TOKEN,
            token ?: ""
        )
    }

    override fun getAutoLoginToken(): String? =
        SharedReferencesUtil.getString(context, SharedReferencesUtil.KEY_AUTO_LOGIN_TOKEN)


    override fun removeToken() {
        SharedReferencesUtil.remove(context, SharedReferencesUtil.KEY_AUTO_LOGIN_TOKEN)
    }

    override fun saveUserId(id: Int) {
        SharedReferencesUtil.setInt(
            context,
            SharedReferencesUtil.KEY_USER_ID,
            id
        )
    }

    override fun getUserId(): Int =
        SharedReferencesUtil.getInt(context, SharedReferencesUtil.KEY_USER_ID, -1)

    override fun removeUserId() {
        SharedReferencesUtil.remove(context, SharedReferencesUtil.KEY_USER_ID)
    }

    override fun isUserSession() =
        SharedReferencesUtil.getBoolean(context, SharedReferencesUtil.KEY_IS_USER_SESSION, true)

    override fun setHostSession() {
        SharedReferencesUtil.setBoolean(context, SharedReferencesUtil.KEY_IS_USER_SESSION, false)
    }

    override fun setUserSession() {
        SharedReferencesUtil.setBoolean(context, SharedReferencesUtil.KEY_IS_USER_SESSION, true)
    }

    override fun getDeviceToken(): String? =
        SharedReferencesUtil.getString(context, SharedReferencesUtil.KEY_DEVICE_TOKEN)

    override fun savePermission(key: String, isChecked: Boolean) {
        SharedReferencesUtil.setBoolean(context, key, isChecked)
    }

    override fun getPermission(key: String): Boolean =
        SharedReferencesUtil.getBoolean(context, key, false)

    @Synchronized
    override fun getUUID(): String {
        var uuid = SharedReferencesUtil.getString(context, SharedReferencesUtil.KEY_UUID)
        if (uuid.isNullOrEmpty()) {
            uuid = UUID.randomUUID().toString()
            SharedReferencesUtil.setString(context, SharedReferencesUtil.KEY_UUID, uuid)
        }
        return uuid
    }

    override fun handleBitmapToSave(uri: Uri): Bitmap? {
        try {
            val picturePath = uri.path
            if (!picturePath.isNullOrEmpty()) {
                val exif = ExifInterface(picturePath)
                val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)
                var bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                val matrix = Matrix()
                when (orientation) {
                    ImageUtil.ORIENTATION_90 -> {
                        matrix.postRotate(ImageUtil.MATRIX_ROTATION_90)
                    }
                    ImageUtil.ORIENTATION_180 -> {
                        matrix.postRotate(ImageUtil.MATRIX_ROTATION_180)
                    }
                    ImageUtil.ORIENTATION_270 -> {
                        matrix.postRotate(ImageUtil.MATRIX_ROTATION_270)
                    }
                }
                bitmap =
                    Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                return bitmap
            }
        } catch (e: Exception) {
            return null
        }
        return null
    }

    @Throws(IOException::class)
    override fun createImageFile(): File? {
        context.cacheDir?.run {
            return File.createTempFile(getUUID(), ".jpg", this)
        }
        return null
    }

    override fun createFileFromBitmap(bitmap: Bitmap): String {
        context.cacheDir?.run {
            val file = File.createTempFile(getUUID(), ".jpg", this)
            return saveImageBitmap(file, bitmap)
        }
        return ""
    }

    private fun saveImageBitmap(file: File, bitmap: Bitmap): String {
        var path: String
        var quality = ImageUtil.MAX_QUALITY_UPLOAD
        do {
            var fileOutputSteam: FileOutputStream? = null
            try {
                fileOutputSteam = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputSteam)
                fileOutputSteam.flush()
                path = file.absolutePath
            } finally {
                fileOutputSteam?.close()
                context.sendBroadcast(
                    Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.fromFile(file)
                    )
                )
            }
            quality -= when {
                file.length() > ImageUtil.LENGTH_IMAGE_HIGH -> ImageUtil.QUALITY_DIV * 5
                file.length() > ImageUtil.LENGTH_IMAGE_MEDIUM -> ImageUtil.QUALITY_DIV * 4
                file.length() > ImageUtil.LENGTH_IMAGE_SHORT -> ImageUtil.QUALITY_DIV * 3
                file.length() > ImageUtil.MAX_LENGTH_IMAGE -> ImageUtil.QUALITY_DIV * 2
                else -> ImageUtil.QUALITY_DIV
            }
        } while (file.length() > ImageUtil.MAX_LENGTH_IMAGE)
        return path
    }
}
