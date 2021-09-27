package com.ctr.homestaybooking.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchBody(
    var address: String? = null,
    var bookingType: BookingType? = null,
    var guestCount: Int? = null,
    var roomCount: Int? = null,
    var bedCount: Int? = null,
    var bathroomCount: Int? = null,
    var minPrice: Double? = 100000.0,
    var maxPrice: Double? = 5000000.0,
    var cancelType: CancelType? = null,
    var status: PlaceStatus? = PlaceStatus.LISTED
) : Parcelable

