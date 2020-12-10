package com.ctr.homestaybooking.data.model

/**
 * Created by at-trinhnguyen2 on 2020/12/07
 */
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "favorites")
@Parcelize
data class Favorite(
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int = -1,

    @SerializedName("place")
    @ColumnInfo(name = "place")
    val place: String
) : Parcelable
