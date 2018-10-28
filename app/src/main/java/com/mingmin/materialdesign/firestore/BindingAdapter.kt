package com.mingmin.materialdesign.firestore

import android.databinding.BindingAdapter
import android.databinding.ObservableField
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.mingmin.materialdesign.R
import me.zhanghai.android.materialratingbar.MaterialRatingBar

@BindingAdapter("app:imageUrl")
fun ImageView.setImageUrl(url: ObservableField<String>) {
    Glide.with(context).load(url.get()).into(this)
}

@BindingAdapter("app:ratingNum")
fun TextView.setRatingNum(num: Int) {
    text = String.format("(%d)", num)
}

@BindingAdapter("app:ratingAvg")
fun MaterialRatingBar.setRatingAvg(avg: Double) {
    rating = avg.toFloat()
}

@BindingAdapter("app:priceLevel")
fun TextView.setPriceLevel(price: Int) {
    val priceLevels = context.resources.getStringArray(R.array.price_levels)
    text = priceLevels.get(price)
}

@BindingAdapter("app:isLoading")
fun View.setLoadingState(isLoading: Boolean) {
    if (isLoading) {
        visibility = View.VISIBLE
    } else {
        visibility = View.GONE
    }
}

@BindingAdapter("app:isEmpty")
fun View.setEmptyState(isEmpty: Boolean) {
    if (isEmpty) {
        visibility = View.VISIBLE
    } else {
        visibility = View.GONE
    }
}

@BindingAdapter("app:visibleOrGone")
fun View.setVisibleOrGone(isShow: Boolean) {
    if (isShow) {
        visibility = View.VISIBLE
    } else {
        visibility = View.GONE
    }
}

@BindingAdapter("app:goneWhenLoading", "app:visibleOrGone")
fun View.setContentVisibleOrGone(isLoading: Boolean, isShow: Boolean) {
    if (isLoading) {
        visibility = View.GONE
    } else {
        if (isShow) {
            visibility = View.VISIBLE
        } else {
            visibility = View.GONE
        }
    }
}
