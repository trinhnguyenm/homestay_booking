package com.ctr.homestaybooking.ui.home.host.place

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.data.source.response.PlaceDetail
import com.ctr.homestaybooking.extension.onClickDelayAction
import kotlinx.android.synthetic.main.adapter_listings_item.view.*

/**
 * Created by at-trinhnguyen2 on 2020/06/06
 */
class HostPlaceAdapter(private val places: List<PlaceDetail>) :
    RecyclerView.Adapter<HostPlaceAdapter.ItemHolder>() {
    internal var onItemClicked: ((item: PlaceDetail) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemHolder(inflater.inflate(R.layout.adapter_listings_item, parent, false))
    }

    override fun getItemCount() = places.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.onBind(places[position])
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.onClickDelayAction {
                onItemClicked?.invoke(places[adapterPosition])
            }
        }

        fun onBind(item: PlaceDetail) {
            itemView.apply {
                tvNameListing.text = item.name
                Glide.with(itemView.context)
                    .load(item.images?.firstOrNull())
                    .into(ivCover)
                tvProgress.text =
                    "Bạn đã hoàn thành ${(item.getSubmitProgress() / 20.0 * 100).toInt()}%"
                progress_finish.progress = item.getSubmitProgress()
            }
        }
    }
}
