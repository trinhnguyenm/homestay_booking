package com.ctr.homestaybooking.data.model

/**
 * Created by at-trinhnguyen2 on 2020/06/21
 */
enum class BookingStatus {
    PENDING,
    ACCEPTED,
    UNPAID,
    PAID,
    COMPLETED,
    CANCELLED
}

internal fun BookingStatus.getText(): String {
    return when (this) {
        BookingStatus.PENDING -> "Đã gửi yêu cầu"
        BookingStatus.ACCEPTED -> "Chưa thanh toán"
        BookingStatus.UNPAID -> "Chưa thanh toán"
        BookingStatus.PAID -> "Chờ check-in"
        BookingStatus.COMPLETED -> "Hoàn thành"
        BookingStatus.CANCELLED -> "Không thành công"
    }
}
