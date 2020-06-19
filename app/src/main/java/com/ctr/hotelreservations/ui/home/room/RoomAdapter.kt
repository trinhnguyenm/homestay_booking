package com.ctr.hotelreservations.ui.home.room

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.data.source.response.RoomResponse
import com.ctr.hotelreservations.extension.getPriceFormat
import com.ctr.hotelreservations.extension.onClickDelayAction
import kotlinx.android.synthetic.main.layout_item_room_of_brand.view.*

/**
 * Created by at-trinhnguyen2 on 2020/06/06
 */
class RoomAdapter(private val rooms: List<RoomResponse.Room>) :
    RecyclerView.Adapter<RoomAdapter.ItemHolder>() {
    internal var onItemClicked: ((item: RoomResponse.Room) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemHolder(inflater.inflate(R.layout.layout_item_room_of_brand, parent, false))
    }

    override fun getItemCount() = rooms.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.onBind(rooms[position])
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.onClickDelayAction {
                onItemClicked?.invoke(rooms[adapterPosition])
            }
        }

        fun onBind(item: RoomResponse.Room) {
            itemView.apply {
                tvTypeRoom.text = item.roomType.name
                tvName.text = resources.getString(R.string.roomNameFormat, item.name)
                tvAddress.text = item.brand.address
                tvRoomInfo.text =
                    item.roomType.capacity.toString() + " guests · " + "size " + item.roomType.size + "㎡"
                Glide.with(itemView.context)
                    .load(item.roomType.thumbnail)
                    .into(ivContent)
                tvPrice.text = item.roomType.price.toString().getPriceFormat()
            }
        }
    }
}
