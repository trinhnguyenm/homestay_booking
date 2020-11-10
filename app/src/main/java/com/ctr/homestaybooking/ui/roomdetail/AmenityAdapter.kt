package com.ctr.homestaybooking.ui.roomdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.data.model.Amenity
import kotlinx.android.synthetic.main.layout_item_amenity.view.*

/**
 * Created by at-trinhnguyen2 on 2020/11/10
 */

class AmenityAdapter(private val amenities: List<Int>) :
    RecyclerView.Adapter<AmenityAdapter.ItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemHolder(inflater.inflate(R.layout.layout_item_amenity, parent, false))
    }

    override fun getItemCount() = amenities.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.onBind(amenities[position])
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(item: Int) {
            itemView.apply {
                Amenity.values().find { it.id == item }?.let {
                    tvAmenity.text = it.amenityName
                    imgAmenity.setImageResource(it.image)
                }
            }
        }
    }
}
