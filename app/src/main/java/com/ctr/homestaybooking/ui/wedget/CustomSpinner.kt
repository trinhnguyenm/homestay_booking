package com.ctr.homestaybooking.ui.wedget

/**
 * Created by at-trinhnguyen2 on 2020/12/03
 */

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import com.ctr.homestaybooking.R
import kotlinx.android.synthetic.main.listing_spinner_item.view.*

/**
 * Created by at-cuongcao on 12/07/2019.
 */
class CustomSpinner(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {

    private var data = listOf<SpinnerItem>()
    internal var onItemSelectedListener: (item: SpinnerItem?) -> Unit = {}
    internal var onItemPositionSelectedListener: (position: Int) -> Unit = {}

    init {
        View.inflate(context, R.layout.listing_spinner_item, this)
        val styleAttrs = context.obtainStyledAttributes(attrs, R.styleable.CustomSpinner)
        try {
            styleAttrs.apply {
                tvTitle.text = styleAttrs.getString(R.styleable.CustomSpinner_title)
            }
            initSpinner(mutableListOf())
        } finally {
            styleAttrs.recycle()
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) = Unit

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                onItemPositionSelectedListener.invoke(p2)
                onItemSelectedListener.invoke(data[p2])
                Log.d("--=", "onItemSelected: ${p2}")
            }
        }
    }

    internal fun initSpinner(data: List<SpinnerItem>, defaultCode: String? = null) {
        this.data = data
        spinner.apply {
            val adapter = ArrayAdapter(
                context,
                R.layout.item_spinner_selected,
                data.map { it.getText() })
            this.adapter = adapter
        }
        if (defaultCode != null) {
            val i = data.indexOfFirst { it.getCode() == defaultCode }
            if (i in data.indices) {
                spinner.setSelection(i)
            }
        }
    }

    internal fun getSelectedPosition() = spinner.selectedItemPosition

    internal fun selectPosition(position: Int) {
        spinner.setSelection(position)
    }

    internal fun selectPosition(code: String? = null) {
        if (code != null) {
            val i = data.indexOfFirst { it.getCode() == code }
            if (i in data.indices) {
                spinner.setSelection(i)
            }
        }
    }

    internal fun getSelectedItem() =
        if (spinner.selectedItemPosition == -1) null else data[spinner.selectedItemPosition]

    internal fun isValid() = spinner.selectedItemPosition != -1

    interface SpinnerItem {
        fun getText(): String

        fun getCode(): String
    }
}
