package com.ctr.homestaybooking.ui.roomdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.data.source.response.Promo
import com.ctr.homestaybooking.extension.onClickDelayAction
import com.ctr.homestaybooking.util.DateUtil
import com.ctr.homestaybooking.util.convert
import kotlinx.android.synthetic.main.layout_item_sale.view.*

/**
 * Created by at-trinhnguyen2 on 2020/06/24
 */
class PromoAdapter(private val promos: List<Promo>) :
    RecyclerView.Adapter<PromoAdapter.ItemHolder>() {
    internal var onItemClicked: ((item: Promo) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemHolder(inflater.inflate(R.layout.layout_item_sale, parent, false))
    }

    override fun getItemCount() = promos.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.onBind(promos[position])
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.onClickDelayAction {
                onItemClicked?.invoke(promos[adapterPosition])
            }
        }

        fun onBind(item: Promo) {
            itemView.apply {
                val startDate = item.startDate.convert(DateUtil.FORMAT_DATE_DAY_MONTH)
                val endDate = item.startDate.convert(DateUtil.FORMAT_DATE_DAY_MONTH)
                tvContent.text =
                    "Sale off ${item.discountPercent}% for booking with check-in date from $startDate to $endDate"
            }
        }
    }
}
