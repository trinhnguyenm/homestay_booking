package com.ctr.homestaybooking.data.source.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/**
 * Created by at-trinhnguyen2 on 2020/06/22
 */
@Parcelize
data class UserResponse(
    @SerializedName("body")
    val userDetail: UserDetail,
    @SerializedName("length")
    val length: Int
) : Parcelable
