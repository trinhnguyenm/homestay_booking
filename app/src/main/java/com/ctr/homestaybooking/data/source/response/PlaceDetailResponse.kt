package com.ctr.homestaybooking.data.source.response

import android.os.Parcelable
import com.ctr.homestaybooking.data.model.DateStatus
import com.ctr.homestaybooking.data.model.PlaceStatus
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/**
 * Created by at-trinhnguyen2 on 2020/11/06
 */
@Parcelize
data class PlaceDetailResponse(
    @SerializedName("body") val placeDetail: PlaceDetail? = null,
    @SerializedName("length") val length: Int
) : Parcelable

@Parcelize
data class PlaceDetail(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("bookingType") val bookingType: String? = null,
    @SerializedName("longitude") val longitude: String? = null,
    @SerializedName("latitude") val latitude: String? = null,
    @SerializedName("street") val street: String? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("guestCount") val guestCount: Int? = null,
    @SerializedName("roomCount") val roomCount: Int? = null,
    @SerializedName("bedCount") val bedCount: Int? = null,
    @SerializedName("bathroomCount") val bathroomCount: Int? = null,
    @SerializedName("size") val size: Int? = null,
    @SerializedName("pricePerDay") val pricePerDay: Double? = null,
    @SerializedName("cancelType") val cancelType: String? = null,
    @SerializedName("earliestCheckInTime") val earliestCheckInTime: String? = null,
    @SerializedName("latestCheckInTime") val latestCheckInTime: String? = null,
    @SerializedName("checkOutTime") val checkOutTime: String? = null,
    @SerializedName("submitStatus") val submitStatus: String? = null,
    @SerializedName("status") val status: PlaceStatus? = null,
    @SerializedName("images") val images: List<String>? = null,
    @SerializedName("amenities") val amenities: List<Int>? = null,
    @SerializedName("bookingSlots") val bookingSlots: List<BookingSlot>? = null,
    @SerializedName("hostDetail") val hostDetail: UserDetail? = null,
    @SerializedName("wardDetail") val wardDetail: WardDetail? = null,
    @SerializedName("placeType") val placeType: String? = null,
    @SerializedName("promos") val promos: List<Promo>? = null,
    @SerializedName("reviews") val reviews: List<Review>? = null,
    @SerializedName("rateCount") val rateCount: Int,
    @SerializedName("rateAverage") val rateAverage: Double
) : Parcelable {
    fun getRooms(): String {
        return "$guestCount khách $roomCount phòng ngủ $bedCount giường $bathroomCount phòng tắm "
    }
}

@Parcelize
data class BookingSlot(
    @SerializedName("date") val date: String,
    @SerializedName("status") val status: DateStatus
) : Parcelable

@Parcelize
data class Promo(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("createDate") val createDate: String,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("discount") val discount: Int? = null,
    @SerializedName("discountPercent") val discountPercent: Int? = null
) : Parcelable

@Parcelize
data class Review(
    @SerializedName("id") val id: Int,
    @SerializedName("comment") val comment: String,
    @SerializedName("rating") val rating: Int,
    @SerializedName("createDate") val createDate: String,
    @SerializedName("userImage") val userImage: String,
    @SerializedName("userName") val userName: String
) : Parcelable


@Parcelize
data class WardDetail(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("districtDetail") val districtDetail: DistrictDetail? = null
) : Parcelable


@Parcelize
data class DistrictDetail(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("province") val province: Province? = null
) : Parcelable

@Parcelize
data class Province(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("name") val name: String? = null
) : Parcelable
