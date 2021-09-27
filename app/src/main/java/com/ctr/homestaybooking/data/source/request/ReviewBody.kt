package com.ctr.homestaybooking.data.source.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/**
 * Created by at-trinhnguyen2 on 2020/12/21
 */
@Parcelize
data class ReviewBody(
    @SerializedName("bookingId") var bookingId: Int? = null,
    @SerializedName("comment") var comment: String? = null,
    @SerializedName("createDate") var createDate: String? = null,
    @SerializedName("rating") var rating: Int? = null
) : Parcelable
