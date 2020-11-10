package com.ctr.homestaybooking.data.source.response

import android.os.Parcelable
import com.ctr.homestaybooking.data.model.BookingType
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/**
 * Created by at-trinhnguyen2 on 2020/11/05
 */
@Parcelize
data class PlaceResponse(
    @SerializedName("body")
    val places: List<Place> = listOf(),
    @SerializedName("length")
    val length: Int? = 0
) : Parcelable

@Parcelize
data class Place(
    @SerializedName("id") val id: Int? = 0,
    @SerializedName("name") val name: String? = "",
    @SerializedName("description") val description: String? = "",
    @SerializedName("bookingType") val bookingType: BookingType? = null,
    @SerializedName("street") val street: String? = "",
    @SerializedName("address") val address: String? = "",
    @SerializedName("guestCount") val guestCount: Int? = 0,
    @SerializedName("roomCount") val roomCount: Int? = 0,
    @SerializedName("bedCount") val bedCount: Int? = 0,
    @SerializedName("bathroomCount") val bathroomCount: Int? = 0,
    @SerializedName("size") val size: Int? = 0,
    @SerializedName("pricePerDay") val pricePerDay: Double? = 0.0,
    @SerializedName("images") val images: List<String>? = listOf(),
    @SerializedName("placeType") val placeType: String? = "",
    @SerializedName("rateCount") val rateCount: Int? = 0,
    @SerializedName("rateAverage") val rateAverage: Double? = 0.0
) : Parcelable
