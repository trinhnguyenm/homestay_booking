package com.ctr.homestaybooking.data.source.request

import android.os.Parcelable
import com.ctr.homestaybooking.data.model.Gender
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/**
 * Created by at-trinhnguyen2 on 2020/06/23
 */
@Parcelize
data class UserBody(
    @SerializedName("email") var email: String? = "",
    @SerializedName("password") var password: String? = "",
    @SerializedName("uuid") var uuid: String? = "",
    @SerializedName("deviceToken") var deviceToken: String? = "",
    @SerializedName("imageUrl") var imageUrl: String? = "",
    @SerializedName("firstName") var firstName: String? = "",
    @SerializedName("lastName") var lastName: String? = "",
    @SerializedName("gender") var gender: Gender? = Gender.OTHER,
    @SerializedName("birthday") var birthday: String? = "1990-01-01T00:00:00.000Z",
    @SerializedName("phoneNumber") var phoneNumber: String? = ""
) : Parcelable
