package com.ctr.homestaybooking.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/**
 * Created by at-trinhnguyen2 on 2020/12/09
 */
@Parcelize
data class ExtraData(
    @SerializedName("bookingId") val bookingId: Int? = null
) : Parcelable
