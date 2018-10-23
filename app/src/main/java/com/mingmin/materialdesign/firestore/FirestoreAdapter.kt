package com.mingmin.materialdesign.firestore

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.mingmin.materialdesign.R
import me.zhanghai.android.materialratingbar.MaterialRatingBar

open class FirestoreAdapter(options: FirestoreRecyclerOptions<RestaurantDoc>) :
        FirestoreRecyclerAdapter<RestaurantDoc, FirestoreAdapter.ViewHolder>(options) {
    class ViewHolder(itemView: View, listener: ItemClickListener?) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.firestore_restaurant_name)
        val city = itemView.findViewById<TextView>(R.id.firestore_restaurant_city)
        val category = itemView.findViewById<TextView>(R.id.firestore_restaurant_category)
        val photo = itemView.findViewById<ImageView>(R.id.firestore_restaurant_image)
        val price = itemView.findViewById<TextView>(R.id.firestore_restaurant_price)
        val ratingNum = itemView.findViewById<TextView>(R.id.firestore_restaurant_rating_num)
        val rating = itemView.findViewById<MaterialRatingBar>(R.id.firestore_restaurant_rating)
        val priceLevels = itemView.context.resources.getStringArray(R.array.price_levels)

        init {
            itemView.setOnClickListener {
                listener?.onItemClick(it.tag as String)
            }
        }
    }

    interface ItemClickListener {
        fun onItemClick(restaurantId: String)
    }

    var mItemClickListener: ItemClickListener? = null

    fun setItemClickListener(listener: ItemClickListener) {
        mItemClickListener = listener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_firestore_restaurant, viewGroup, false)
        return ViewHolder(view, mItemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: RestaurantDoc) {
        holder.itemView.tag = snapshots.getSnapshot(position).id
        holder.name.text = model.name
        holder.city.text = model.city
        holder.category.text = model.category
        Glide.with(holder.photo.context)
                .load(model.photo)
                .into(holder.photo)
        holder.price.text = holder.priceLevels.get(model.price)
        holder.ratingNum.text = String.format("(%d)", model.ratingNum)
        holder.rating.rating = model.ratingAvg.toFloat()
    }
}