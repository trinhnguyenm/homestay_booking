package com.ctr.homestaybooking.data.source.request

import android.os.Parcelable
import com.ctr.homestaybooking.data.model.BookingStatus
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/**
 * Created by at-trinhnguyen2 on 2020/11/12
 */
@Parcelize
data class BookingBody(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("status") var status: BookingStatus? = null,
    @SerializedName("startDate") var startDate: String? = null,
    @SerializedName("endDate") var endDate: String? = null,
    @SerializedName("pricePerDay") var pricePerDay: Double? = null,
    @SerializedName("priceForStay") var priceForStay: Double? = null,
    @SerializedName("taxPaid") var taxPaid: Double? = null,
    @SerializedName("totalPaid") var totalPaid: Double? = null,
    @SerializedName("promoId") var promoId: Int? = null,
    @SerializedName("placeId") var placeId: Int? = null,
    @SerializedName("userId") var userId: Int? = null
) : Parcelable
