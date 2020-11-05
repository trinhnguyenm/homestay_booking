package com.ctr.homestaybooking.data.source

import android.content.Context
import com.ctr.homestaybooking.data.source.datasource.LocalDataSource
import com.ctr.homestaybooking.util.SharedReferencesUtil
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

    override fun saveUserId(id: Int) {
        SharedReferencesUtil.setInt(
            context,
            SharedReferencesUtil.KEY_USER_ID,
            id
        )
    }

    override fun getUserId(): Int =
        SharedReferencesUtil.getInt(context, SharedReferencesUtil.KEY_USER_ID, -1)

    override fun removeToken() {
        SharedReferencesUtil.remove(context, SharedReferencesUtil.KEY_AUTO_LOGIN_TOKEN)
    }

    override fun removeUserId() {
        SharedReferencesUtil.remove(context, SharedReferencesUtil.KEY_USER_ID)
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
