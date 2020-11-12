package com.ctr.homestaybooking.data.source.response

import android.os.Parcelable
import com.ctr.homestaybooking.data.model.BookingStatus
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/**
 * Created by at-trinhnguyen2 on 2020/11/12
 */
@Parcelize
data class BookingResponse(
    @SerializedName("body") val booking: Booking,
    @SerializedName("length") val length: Int
) : Parcelable

@Parcelize
data class Booking(
    @SerializedName("id") val id: Int,
    @SerializedName("createDate") val createDate: String,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("pricePerDay") val pricePerDay: Double,
    @SerializedName("priceForStay") val priceForStay: Double,
    @SerializedName("taxPaid") val taxPaid: Double,
    @SerializedName("totalPaid") val totalPaid: Double,
    @SerializedName("cancerDate") val cancerDate: String? = null,
    @SerializedName("refund") val refund: Boolean? = null,
    @SerializedName("refundPaid") val refundPaid: Double? = null,
    @SerializedName("status") val status: BookingStatus,
    @SerializedName("place") val place: Place,
    @SerializedName("user") val user: UserDetail,
    @SerializedName("promo") val promo: Promo? = null,
    @SerializedName("review") val review: Review? = null
) : Parcelable
