package com.ctr.homestaybooking.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_responses")
data class LocalResponse(
    @PrimaryKey
    @ColumnInfo(name = "url")
    val requestUrl: String,

    @ColumnInfo(name = "response")
    val response: String
)
