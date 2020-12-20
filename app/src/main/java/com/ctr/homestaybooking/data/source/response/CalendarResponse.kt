package com.ctr.homestaybooking.data.source.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class CalendarResponse(
    @SerializedName("body") val url: String,
    @SerializedName("length") val length: Int
) : Parcelable
