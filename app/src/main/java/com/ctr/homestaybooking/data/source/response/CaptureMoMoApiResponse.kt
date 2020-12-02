package com.ctr.homestaybooking.data.source.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Created by at-trinhnguyen2 on
 */
@Parcelize
data class CaptureMoMoApiResponse(
    @SerializedName("body")
    val body: CaptureMoMoResponse,
    @SerializedName("length")
    val length: Int? = null
) : Parcelable

@Parcelize
data class CaptureMoMoResponse(
    @SerializedName("partnerCode") val partnerCode: String? = null,
    @SerializedName("orderId") val orderId: String? = null,
    @SerializedName("orderInfo") val orderInfo: String? = null,
    @SerializedName("accessKey") val accessKey: String? = null,
    @SerializedName("amount") val amount: String? = null,
    @SerializedName("signature") val signature: String? = null,
    @SerializedName("extraData") val extraData: String? = null,
    @SerializedName("requestId") val requestId: String? = null,
    @SerializedName("notifyUrl") val notifyUrl: String? = null,
    @SerializedName("returnUrl") val returnUrl: String? = null,
    @SerializedName("requestType") val requestType: String? = null,
    @SerializedName("errorCode") val errorCode: Int? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("localMessage") val localMessage: String? = null,
    @SerializedName("transId") val transId: String? = null,
    @SerializedName("orderType") val orderType: String? = null,
    @SerializedName("payType") val payType: String? = null,
    @SerializedName("payUrl") val payUrl: String? = null,
    @SerializedName("deeplink") val deeplink: String? = null,
    @SerializedName("deeplinkWebInApp") val deeplinkWebInApp: String? = null,
    @SerializedName("qrCodeUrl") val qrCodeUrl: String? = null,
    @SerializedName("responseDate") val responseDate: Date? = null,
    @SerializedName("jsonObject") val jsonObject: String? = null
) : Parcelable
