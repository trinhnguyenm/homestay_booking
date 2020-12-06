package com.ctr.homestaybooking.data.source.response

import android.os.Parcelable
import com.ctr.homestaybooking.data.model.Role
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/**
 * Created by at-trinhnguyen2 on 2020/06/17
 */

@Parcelize
data class LoginResponse(
    @SerializedName("body") val authToken: AuthToken,
    @SerializedName("length") val length: Int
) : Parcelable

@Parcelize
data class AuthToken(
    @SerializedName("token") val token: String,
    @SerializedName("userDetail") val userDetail: UserDetail
) : Parcelable

@Parcelize
data class UserDetail(
    @SerializedName("id") val id: Int,
    @SerializedName("email") val email: String,
    @SerializedName("uuid") val uuid: String,
    @SerializedName("deviceToken") val deviceToken: String,
    @SerializedName("roles") val roles: List<Role>,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("birthday") val birthday: String,
    @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("status") val status: String
) : Parcelable {
    fun getName() = "$firstName $lastName"
}
