package com.ctr.homestaybooking.data.model

import androidx.annotation.DrawableRes
import com.ctr.homestaybooking.R

/**
 * Created by at-trinhnguyen2 on 2020/11/10
 */
enum class Amenity(var id: Int, var amenityName: String, @DrawableRes var image: Int) {
    WIFI(1, "Wifi", R.drawable.ic_lst_icon_hotel_wifi),
    TV(2, "TV", R.drawable.ic_lst_icon_tv),
    AIR_CONDITIONING(3, "Điều hòa", R.drawable.ic_lst_icon_air_conditioning),
    REFRIGERATOR(4, "Tủ lạnh", R.drawable.ic_lst_icon_refrigerator),
    DRYER(5, "Máy sấy", R.drawable.ic_lst_icon_hair_dryer),
    SHAMPOO(6, "Dầu gội, dầu xả", R.drawable.ic_lst_icon_shampoo),
    TOILET_PAPER(7, "Giấy vệ sinh", R.drawable.ic_lst_icon_toilet_paper),
    TOWEL(8, "Khăn tắm", R.drawable.ic_lst_icon_towels),
    JACUZZI(9, "Bồn tắm", R.drawable.ic_lst_icon_jacuzzi),
    TOOTHPASTE(10, "Kem đánh răng", R.drawable.ic_lst_icon_toothpaste),
    SOAP(12, "Xà phòng tắm", R.drawable.ic_lst_icon_soap),
    INTERNET(12, "Internet", R.drawable.ic_icon_internet),
    TISSUE(13, "Khăn giấy", R.drawable.ic_lst_icon_tissues),
    BOTTLED_WATER(14, "Bình nước", R.drawable.ic_lst_icon_complimentary_bottled_water),
    HANGER(15, "Móc treo quần áo", R.drawable.ic_lst_icon_hanger),
    CLOSET(16, "Tủ quần áo", R.drawable.ic_lst_icon_closet),
    IRON(17, "Bàn ủi", R.drawable.ic_lst_icon_iron),
    MIRROR(18, "Bàn trang điểm", R.drawable.ic_lst_icon_mirror),
    KITCHEN(19, "Nhà bếp", R.drawable.ic_icon_kitchen),
    MICROWAVE(20, "Lò vi sóng", R.drawable.ic_lst_icon_microwave),
    STOVE_ELECTRIC(21, "Bếp điện", R.drawable.ic_lst_icon_stove_electric),
    STOVE_GAS(22, "Bếp gas", R.drawable.ic_lst_icon_stove_gas),
    WASHER(23, "Máy giặc", R.drawable.ic_lst_icon_washer),
    COFFEE_POT(24, "Máy pha cà phê", R.drawable.ic_lst_icon_coffee_pot),
    KARAOKE(25, "Karaoke", R.drawable.ic_lst_icon_karaoke),
    POOL(26, "Bể bơi", R.drawable.ic_lst_icon_private_pool),
    DESK(27, "Bàn làm việc", R.drawable.ic_lst_icon_desk),
}
