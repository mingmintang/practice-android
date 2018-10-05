package com.mingmin.materialdesign

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class MainRecyclerViewAdapter(val demos: ArrayList<Demo>) : RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder>() {
    lateinit var clickListener: OnItemClickListener

    class ViewHolder(itemView: View, clickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.main_item_title)
        val desc: TextView = itemView.findViewById(R.id.main_item_desc)

        init {
            itemView.setOnClickListener {
                clickListener.onItemClicked(it, adapterPosition)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(view: View, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        clickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_main, parent, false)
        return ViewHolder(view, clickListener)
    }

    override fun getItemCount(): Int {
        return demos.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val demo = demos.get(position)
        holder.title.text = demo.title
        holder.desc.text = demo.desc
    }
}