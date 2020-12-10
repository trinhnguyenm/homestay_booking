package com.ctr.homestaybooking.data.source.request

import com.ctr.homestaybooking.data.model.BookingType
import com.ctr.homestaybooking.data.model.CancelType
import com.ctr.homestaybooking.data.model.PlaceStatus
import com.ctr.homestaybooking.data.model.SubmitStatus
import com.ctr.homestaybooking.data.source.response.BookingSlot
import com.google.gson.annotations.SerializedName

/**
 * Created by at-trinhnguyen2 on 2020/12/03
 */
data class PlaceBody(

    @SerializedName("id") var id: Int = 0,

    @SerializedName("name") var name: String? = null,

    @SerializedName("description") var description: String? = null,

    @SerializedName("bookingType") var bookingType: BookingType? = null,

    @SerializedName("longitude") var longitude: String? = null,

    @SerializedName("latitude") var latitude: String? = null,

    @SerializedName("street") var street: String? = null,

    @SerializedName("address") var address: String? = null,

    @SerializedName("guestCount") var guestCount: Int? = null,

    @SerializedName("roomCount") var roomCount: Int? = null,

    @SerializedName("bedCount") var bedCount: Int? = null,

    @SerializedName("bathroomCount") var bathroomCount: Int? = null,

    @SerializedName("size") var size: Int? = null,

    @SerializedName("pricePerDay") var pricePerDay: Double? = null,

    @SerializedName("cancelType") var cancelType: CancelType? = null,

    @SerializedName("earliestCheckInTime") var earliestCheckInTime: String? = null,

    @SerializedName("latestCheckInTime") var latestCheckInTime: String? = null,

    @SerializedName("checkOutTime") var checkOutTime: String? = null,

    @SerializedName("submitStatus") var submitStatus: SubmitStatus = SubmitStatus.DRAFT,

    @SerializedName("status") var status: PlaceStatus = PlaceStatus.UNLISTED,

    @SerializedName("images") var images: MutableList<String>? = null,

    @SerializedName("amenities") var amenities: MutableList<Int>? = null,

    @SerializedName("bookingSlots") var bookingSlots: MutableList<BookingSlot>? = null,

    @SerializedName("userId") var userId: Int? = null,

    @SerializedName("wardId") var wardId: Int? = null,

    @SerializedName("placeTypeId") var placeTypeId: Int? = null

)
