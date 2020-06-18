package com.ctr.hotelreservations.data.source

import android.content.Context
import com.ctr.hotelreservations.data.source.datasource.LocalDataSource
import com.ctr.hotelreservations.util.SharedReferencesUtil
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

    override fun getDeviceToken(): String? =
        SharedReferencesUtil.getString(context, SharedReferencesUtil.KEY_DEVICE_TOKEN)

    @Synchronized
    override fun getUUID(): String {
        var uuid = SharedReferencesUtil.getString(context, SharedReferencesUtil.KEY_UUID)
        if (uuid.isNullOrEmpty()) {
            uuid = UUID.randomUUID().toString()
            SharedReferencesUtil.setString(context, SharedReferencesUtil.KEY_UUID, uuid)
        }
        return uuid
    }
}
