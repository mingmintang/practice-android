package com.mingmin.practice.firestore

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.mingmin.practice.R
import com.mingmin.practice.firestore.firestore.RatingDoc
import me.zhanghai.android.materialratingbar.MaterialRatingBar
import java.text.SimpleDateFormat
import java.util.*

open class RatingsAdapter(options: FirestoreRecyclerOptions<RatingDoc>) : FirestoreRecyclerAdapter<RatingDoc, RatingsAdapter.ViewHolder>(options) {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.firestore_rating_name)
        val date = itemView.findViewById<TextView>(R.id.firestore_rating_date)
        val rating = itemView.findViewById<MaterialRatingBar>(R.id.firestore_rating_rating)
        val comment = itemView.findViewById<TextView>(R.id.firestore_rating_comment)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_firestore_rating, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: RatingDoc) {
        holder.name.text = model.userName
        model.timestamp?.let {
            holder.date.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it)
        }
        holder.rating.rating = model.rating.toFloat()
        holder.comment.text = model.comment
    }
}