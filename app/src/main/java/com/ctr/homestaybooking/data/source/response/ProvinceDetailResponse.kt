package com.ctr.homestaybooking.data.source.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by at-trinhnguyen2 on 2020/12/04
 */

@Parcelize
data class ProvinceDetailResponse(
    @SerializedName("body") val provinceDetail: ProvinceDetail,
    @SerializedName("length") val length: Int
) : Parcelable

@Parcelize
data class ProvinceDetail(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("districts") val districts: List<District>? = null
) : Parcelable

@Parcelize
data class District(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("wards") val wards: List<Ward>? = null
) : Parcelable {
    internal fun getName() = "$type $name"
}

@Parcelize
data class Ward(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("name") val name: String? = null
) : Parcelable {
    internal fun getName() = "$type $name"
}

