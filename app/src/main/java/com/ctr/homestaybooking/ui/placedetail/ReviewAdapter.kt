package com.ctr.homestaybooking.ui.placedetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.data.source.response.Review
import com.ctr.homestaybooking.extension.onClickDelayAction
import com.ctr.homestaybooking.util.DateUtil
import com.ctr.homestaybooking.util.convert
import kotlinx.android.synthetic.main.layout_item_review.view.*

/**
 * Created by at-trinhnguyen2 on 2020/12/21
 */

class ReviewAdapter(private val reviews: List<Review>) :
    RecyclerView.Adapter<ReviewAdapter.ItemHolder>() {
    internal var onItemClicked: ((item: Review) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemHolder(inflater.inflate(R.layout.layout_item_review, parent, false))
    }

    override fun getItemCount() = reviews.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.onBind(reviews[position])
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.onClickDelayAction {
                onItemClicked?.invoke(reviews[adapterPosition])
            }
        }

        fun onBind(item: Review) {
            itemView.apply {
                Glide.with(itemView.context)
                    .load(item.userImage)
                    .into(imgUser)
                tvUsername.text = item.userName
                tvComment.text = item.comment
                tvTime.text = item.createDate.convert(DateUtil.FORMAT_DATE)
                ratingBar.rating = item.rating.toFloat()
            }
        }
    }
}
