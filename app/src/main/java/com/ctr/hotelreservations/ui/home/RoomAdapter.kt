package com.ctr.hotelreservations.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.data.model.RoomInfo
import com.ctr.hotelreservations.extension.onClickDelayAction
import kotlinx.android.synthetic.main.layout_item_room_of_host.view.*

/**
 * Created by at-trinhnguyen2 on 2020/06/06
 */
class RoomAdapter(private val roomInfos: List<RoomInfo>) :
    RecyclerView.Adapter<RoomAdapter.ItemHolder>() {
    internal var onItemClicked: ((item: RoomInfo) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemHolder(inflater.inflate(R.layout.layout_item_room_of_host, parent, false))
    }

    override fun getItemCount() = roomInfos.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.onBind(roomInfos[position])
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.onClickDelayAction {
                onItemClicked?.invoke(roomInfos[adapterPosition])
            }
        }

        fun onBind(item: RoomInfo) {
            itemView.apply {
                itemView.tvName.text = item.title
            }
        }
    }
}