package com.ctr.homestaybooking.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by at-trinhnguyen2 on 2020/12/06
 */
@Parcelize
class ImageSlideData(
    val images: List<String>,
    val currentPosition: Int
) :
    Parcelable
