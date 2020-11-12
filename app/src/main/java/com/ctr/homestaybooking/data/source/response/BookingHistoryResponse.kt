package com.ctr.homestaybooking.data.source.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class BookingHistoryResponse(
    @SerializedName("body") val bookings: List<Booking>,
    @SerializedName("length") val length: Int? = null
) : Parcelable
