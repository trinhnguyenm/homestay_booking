package com.ctr.hotelreservations.data.source.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/**
 * Created by at-trinhnguyen2 on 2020/06/17
 */

@Parcelize
data class LoginResponse(
    @SerializedName("body") val body: Body,
    @SerializedName("length") val length: Int
) : Parcelable

@Parcelize
data class Body(
    @SerializedName("token") val token: String
) : Parcelable