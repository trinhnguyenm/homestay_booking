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
    @SerializedName("body") val places: List<Place>,
    @SerializedName("length") val length: Int
) : Parcelable

@Parcelize
data class Place(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("bookingType") val bookingType: BookingType? = null,
    @SerializedName("street") val street: String? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("guestCount") val guestCount: Int? = null,
    @SerializedName("roomCount") val roomCount: Int? = null,
    @SerializedName("bedCount") val bedCount: Int? = null,
    @SerializedName("bathroomCount") val bathroomCount: Int? = null,
    @SerializedName("size") val size: Int? = null,
    @SerializedName("pricePerDay") val pricePerDay: Double? = null,
    @SerializedName("images") val images: List<String>? = listOf(),
    @SerializedName("placeType") val placeType: PlaceType? = null,
    @SerializedName("rateCount") val rateCount: Int? = null,
    @SerializedName("rateAverage") val rateAverage: Double? = null,
    var isLike: Boolean = false,
    var likedTime: String? = null
) : Parcelable {
    fun getRooms(): String {
        return "$guestCount khách $roomCount phòng ngủ $bedCount giường $bathroomCount phòng tắm "
    }
}
