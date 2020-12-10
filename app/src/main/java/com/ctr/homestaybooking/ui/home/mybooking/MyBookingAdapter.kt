package com.ctr.homestaybooking.ui.home.mybooking

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.data.model.BookingStatus
import com.ctr.homestaybooking.data.model.getText
import com.ctr.homestaybooking.data.source.response.Booking
import com.ctr.homestaybooking.extension.onClickDelayAction
import com.ctr.homestaybooking.extension.toMoney
import com.ctr.homestaybooking.util.FORMAT_DATE_API
import com.ctr.homestaybooking.util.format
import com.ctr.homestaybooking.util.toCalendar
import kotlinx.android.synthetic.main.layout_item_my_booking.view.*

/**
 * Created by at-trinhnguyen2 on 2020/06/24
 */
@SuppressLint("SetTextI18n")
class MyBookingAdapter(private val bookings: List<Booking>) :
    RecyclerView.Adapter<MyBookingAdapter.ItemHolder>() {
    internal var onItemClicked: ((item: Booking) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemHolder(inflater.inflate(R.layout.layout_item_my_booking, parent, false))
    }

    override fun getItemCount() = bookings.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.onBind(bookings[position])
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.onClickDelayAction {
                onItemClicked?.invoke(bookings[adapterPosition])
            }
        }

        fun onBind(item: Booking) {
            itemView.apply {
                Glide.with(itemView.context).load(item.place.images?.firstOrNull())
                    .into(ivPlaceThumb)
                tvBookingId.text = "Mã đơn: ${item.id}"
                tvTotalPrize.text = item.totalPaid.toMoney()
                tvPlaceType.text = item.place.placeType?.name
                tvPlaceName.text = item.place.name
                tvPlaceAddress.text = item.place.address
                tvCheckInDay.text =
                    item.startDate.toCalendar(FORMAT_DATE_API).format("dd.MM.yyyy") + " - " +
                            item.endDate.toCalendar(FORMAT_DATE_API).format("dd.MM.yyyy")
                tvBookingStatus.text = item.status.getText()
                tvBookingStatus.setTextColor(
                    when (item.status) {
                        BookingStatus.PENDING -> {
                            resources.getColor(R.color.colorAccent)
                        }
                        BookingStatus.ACCEPTED -> {
                            resources.getColor(R.color.colorAccent)
                        }
                        BookingStatus.UNPAID -> {
                            resources.getColor(R.color.colorAccent)
                        }
                        BookingStatus.PAID -> {
                            resources.getColor(R.color.colorAccent)
                        }
                        BookingStatus.COMPLETED -> {
                            resources.getColor(R.color.booking_status_complete)
                        }
                        else -> {
                            resources.getColor(R.color.booking_status_incomplete)
                        }
                    }
                )
            }
        }
    }
}
