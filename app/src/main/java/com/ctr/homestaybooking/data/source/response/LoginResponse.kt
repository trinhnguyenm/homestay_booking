package com.ctr.homestaybooking.data.source.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/**
 * Created by at-trinhnguyen2 on 2020/06/17
 */

@Parcelize
data class LoginResponse(
    @SerializedName("body")
    val authToken: AuthToken,
    @SerializedName("length")
    val length: Int
) : Parcelable

@Parcelize
data class AuthToken(
    @SerializedName("token")
    val token: String,
    @SerializedName("userDetailDto")
    val userDetailDto: UserDetailDto
) : Parcelable

@Parcelize
data class UserDetailDto(
    @SerializedName("birthday")
    val birthday: String,
    @SerializedName("deviceToken")
    val deviceToken: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("roles")
    val roles: List<String>,
    @SerializedName("status")
    val status: String,
    @SerializedName("uuid")
    val uuid: String
) : Parcelable
