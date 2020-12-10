package com.ctr.homestaybooking.data.source.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/**
 * Created by at-trinhnguyen2 on 2020/11/05
 */
@Parcelize
data class HostPlaceResponse(
    @SerializedName("body")
    val places: List<PlaceDetail> = listOf(),
    @SerializedName("length")
    val length: Int? = 0
) : Parcelable
