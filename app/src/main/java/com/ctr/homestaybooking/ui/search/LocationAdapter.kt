package com.ctr.homestaybooking.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.extension.onClickDelayAction
import kotlinx.android.synthetic.main.item_popular_location.view.*

/**
 * Created by at-trinhnguyen2 on 2020/12/21
 */

class LocationAdapter(private val locations: List<Location>) :
    RecyclerView.Adapter<LocationAdapter.ItemHolder>() {
    internal var onItemClicked: ((item: Location) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemHolder(inflater.inflate(R.layout.item_popular_location, parent, false))
    }

    override fun getItemCount() = locations.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.onBind(locations[position])
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.onClickDelayAction {
                onItemClicked?.invoke(locations[adapterPosition])
            }
        }

        fun onBind(item: Location) {
            itemView.apply {
                Glide.with(itemView.context)
                    .load(item.image)
                    .into(imgLocation)
                tvLocation.text = item.name
            }
        }
    }
}
