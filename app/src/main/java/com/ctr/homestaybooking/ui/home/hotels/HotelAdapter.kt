package com.ctr.homestaybooking.ui.home.hotels

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.data.source.response.Place
import com.ctr.homestaybooking.extension.format
import com.ctr.homestaybooking.extension.gone
import com.ctr.homestaybooking.extension.onClickDelayAction
import com.ctr.homestaybooking.extension.toMoney
import kotlinx.android.synthetic.main.layout_item_place.view.*

/**
 * Created by at-trinhnguyen2 on 2020/06/06
 */
class HotelAdapter(private val places: List<Place>) :
    RecyclerView.Adapter<HotelAdapter.ItemHolder>() {
    internal var onItemClicked: ((item: Place) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemHolder(inflater.inflate(R.layout.layout_item_place, parent, false))
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

        fun onBind(item: Place) {
            itemView.apply {
                tvPlaceType.text = item.placeType
                tvName.text = item.name
                tvAddress.text = item.address
                tvRoomInfo.text =
                    "${item.guestCount} khách · ${item.bedCount} phòng ngủ · ${item.bathroomCount} phòng tắm"
                Glide.with(itemView.context)
                    .load(item.images?.firstOrNull())
                    .into(ivThumbnail)
                tvPrice.text = "${item.pricePerDay?.toMoney()}/đêm"
                if (item.rateCount == 0) {
                    imgRatingStar.gone()
                    tvRateAverage.gone()
                    tvRateCount.gone()
                } else {
                    tvRateAverage.text = item.rateAverage?.format(2)
                    tvRateCount.text = "(${item.rateCount})"
                }
            }
        }
    }
}
