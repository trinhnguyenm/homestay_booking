package com.ctr.homestaybooking.ui.wedget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.extension.gone
import kotlinx.android.synthetic.main.listing_info_item.view.*

/**
 * Created by at-trinhnguyen2 on 2020/12/03
 */
class ListingInfoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private var isDone = false
    private var isShowUpdate = false

    init {
        initView(attrs)
    }

    internal fun isDone() = isDone

    internal fun isShowUpdate() = isShowUpdate

    internal fun setCheckVisible(visible: Int) {
        ivCheck.visibility = visible
    }

    internal fun setCompleted(isCompleted: Boolean) {
        ivCheck.setImageResource(if (isCompleted) R.drawable.ic_check else R.drawable.ic_check_gray)
        isDone = isCompleted
        tvUpdate.gone()
        isEnabled = isCompleted
        if (isCompleted) {
            ivCheck.alpha = 1f
            tvInfoTitle.alpha = 1f
        } else {
            ivCheck.alpha = 0.5f
            tvInfoTitle.alpha = 0.5f
        }
    }

    internal fun setVisibleUpdate(isVisible: Boolean) {
        tvUpdate.visibility = if (isVisible) View.VISIBLE else View.GONE
        isShowUpdate = true
        isEnabled = isVisible
        if (isVisible) {
            ivCheck.alpha = 1.0f
            tvInfoTitle.alpha = 1.0f
        } else {
            ivCheck.alpha = 0.5f
            tvInfoTitle.alpha = 0.5f
        }
    }

    internal fun setUpdateText(text: String?) {
        tvUpdate.text = text
    }

    private fun initView(attrs: AttributeSet?) {
        View.inflate(context, R.layout.listing_info_item, this)
        val styleAttrs = context.obtainStyledAttributes(attrs, R.styleable.ListingInfoView)
        try {
            tvInfoTitle.text = styleAttrs.getString(R.styleable.ListingInfoView_titleText)
            setCompleted(styleAttrs.getBoolean(R.styleable.ListingInfoView_isCompleted, false))
        } finally {
            styleAttrs.recycle()
        }
    }
}
