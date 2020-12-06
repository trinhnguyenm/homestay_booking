package com.ctr.homestaybooking.data.source.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/**
 * Created by at-trinhnguyen2 on 2020/12/04
 */
@Parcelize
data class PlaceTypeResponse(
    @SerializedName("body") val placeTypes: List<PlaceType>,
    @SerializedName("length") val length: Int
) : Parcelable

@Parcelize
data class PlaceType(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String
) : Parcelable
