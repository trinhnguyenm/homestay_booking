package com.ctr.homestaybooking.data.source.response

import android.os.Parcelable
import android.util.Log
import com.ctr.homestaybooking.data.model.*
import com.ctr.homestaybooking.data.source.request.PlaceBody
import com.ctr.homestaybooking.util.format
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*


/**
 * Created by at-trinhnguyen2 on 2020/11/06
 */
@Parcelize
data class PlaceDetailResponse(
    @SerializedName("body") val placeDetail: PlaceDetail,
    @SerializedName("length") val length: Int
) : Parcelable

@Parcelize
data class PlaceDetail(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("bookingType") val bookingType: BookingType? = null,
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
    @SerializedName("cancelType") val cancelType: CancelType? = null,
    @SerializedName("earliestCheckInTime") val earliestCheckInTime: String? = null,
    @SerializedName("latestCheckInTime") val latestCheckInTime: String? = null,
    @SerializedName("checkOutTime") val checkOutTime: String? = null,
    @SerializedName("submitStatus") val submitStatus: SubmitStatus? = null,
    @SerializedName("status") val status: PlaceStatus? = null,
    @SerializedName("images") val images: List<String>? = null,
    @SerializedName("amenities") val amenities: List<Int>? = null,
    @SerializedName("bookingSlots") val bookingSlots: List<BookingSlot>? = null,
    @SerializedName("hostDetail") val hostDetail: UserDetail? = null,
    @SerializedName("wardDetail") val wardDetail: WardDetail? = null,
    @SerializedName("placeType") val placeType: PlaceType? = null,
    @SerializedName("promos") val promos: List<Promo>? = null,
    @SerializedName("reviews") val reviews: List<Review>? = null,
    @SerializedName("rateCount") val rateCount: Int,
    @SerializedName("rateAverage") val rateAverage: Double
) : Parcelable {
    internal fun getRooms(): String {
        val result = StringBuilder()
        if (guestCount != 0) result.append("$guestCount khách ")
        if (roomCount != 0) result.append("$roomCount phòng ngủ ")
        if (bedCount != 0) result.append("$bedCount giường ")
        if (bathroomCount != 0) result.append("$bathroomCount phòng tắm ")
        return result.toString()
    }

    internal fun isSubmitBasicInfo() =
        listOf(
            name,
            description,
            bookingType,
            placeType,
            wardDetail,
            street,
            size,
            guestCount,
            roomCount,
            bedCount,
            bathroomCount
        ).all { it != null }

    internal fun isSubmitImages(): Boolean {
        images.apply { Log.d("--=", "images+${this}") }
        return isSubmitBasicInfo() && images != null && images.size >= 4
    }

    internal fun isSubmitPrice() =
        isSubmitImages() &&
                listOf(
                    pricePerDay,
                    cancelType
                ).all { it != null }

    internal fun isSubmitCalendar() = isSubmitPrice() && !bookingSlots.isNullOrEmpty()

    internal fun getSubmitProgressPercent(): Int {
        val list = listOf(
            name,
            description,
            bookingType,
            street,
            address,
            guestCount,
            roomCount,
            bedCount,
            bathroomCount,
            size,
            pricePerDay,
            cancelType,
            earliestCheckInTime,
            latestCheckInTime,
            checkOutTime,
            images,
            amenities,
            wardDetail,
            placeType
        )
        return (list.count { it != null }.div(list.size.toDouble()) * 100).toInt()
    }

    internal fun toPlaceBody() = PlaceBody(
        id = id,
        name = name,
        description = description,
        bookingType = bookingType,
        longitude = longitude,
        latitude = latitude,
        street = street,
        address = address,
        guestCount = guestCount,
        roomCount = roomCount,
        bedCount = bedCount,
        bathroomCount = bathroomCount,
        size = size,
        pricePerDay = pricePerDay,
        cancelType = cancelType,
        earliestCheckInTime = earliestCheckInTime,
        latestCheckInTime = latestCheckInTime,
        checkOutTime = checkOutTime,
        submitStatus = submitStatus ?: SubmitStatus.DRAFT,
        status = status ?: PlaceStatus.UNLISTED,
        images = images?.toMutableList(),
        amenities = amenities?.toMutableList(),
        bookingSlots = bookingSlots?.toMutableList(),
        userId = hostDetail?.id,
        wardId = wardDetail?.id,
        placeTypeId = placeType?.id
    )

    internal fun toPlace() = Place(
        id,
        name,
        description,
        bookingType,
        street,
        address,
        guestCount,
        roomCount,
        bedCount,
        bathroomCount,
        size,
        pricePerDay,
        images,
        placeType,
        rateCount,
        rateAverage,
        true,
        Calendar.getInstance().format()
    )
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
    @SerializedName("id") val id: Int,
    @SerializedName("type") val type: String,
    @SerializedName("name") val name: String
) : Parcelable {
    internal fun getName() = "$type $name"
}
