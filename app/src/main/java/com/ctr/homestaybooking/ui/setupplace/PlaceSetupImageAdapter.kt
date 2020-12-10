package com.ctr.homestaybooking.ui.setupplace

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.extension.gone
import com.ctr.homestaybooking.extension.onClickDelayAction
import com.ctr.homestaybooking.extension.visible
import kotlinx.android.synthetic.main.adapter_photo_image_item.view.*

/**
 * Created by at-trinhnguyen2 on 2020/12/05
 */

class PlaceSetupImageAdapter(private val images: List<String>) :
    RecyclerView.Adapter<PlaceSetupImageAdapter.ItemHolder>() {
    internal var onItemClicked: ((position: Int) -> Unit)? = null
    internal var onItemDeleteClicked: ((position: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemHolder(inflater.inflate(R.layout.adapter_photo_image_item, parent, false))
    }

    override fun getItemCount() = images.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.onBind(images[position])
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.ivDelete.onClickDelayAction {
                onItemDeleteClicked?.invoke(adapterPosition)
            }
            itemView.ivContent.onClickDelayAction {
                onItemClicked?.invoke(adapterPosition)
            }
        }

        fun onBind(item: String) {
            itemView.apply {
                Glide.with(itemView.context).load(item).into(ivContent)
                if (adapterPosition == 0) {
                    tvCover.visible()
                } else {
                    tvCover.gone()
                }
            }
        }
    }
}
