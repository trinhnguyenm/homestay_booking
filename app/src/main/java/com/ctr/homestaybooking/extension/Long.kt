package com.ctr.homestaybooking.extension

import java.text.NumberFormat
import java.util.*

/**
 * Created by at-trinhnguyen2 on 2020/11/06
 */
fun Double?.toMoney() = NumberFormat.getInstance(Locale("nv", "VN")).format(this).plus("₫")

fun Double.format(digits: Int) = "%.${digits}f".format(this)
