package com.ctr.homestaybooking.data.source.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/**
 * Created by at-trinhnguyen2 on 2020/12/04
 */
@Parcelize
data class ProvinceResponse(
    @SerializedName("body") val provinces: List<Province>,
    @SerializedName("length") val length: Int
) : Parcelable
