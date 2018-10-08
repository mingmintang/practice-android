package com.mingmin.materialdesign.bottomsheet

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.mingmin.materialdesign.R

class BottomSheetAdapter(val versions: ArrayList<String>) : RecyclerView.Adapter<BottomSheetAdapter.ViewHolder>() {
    class ViewHolder(itemView: View, clickListener: ItemClickListener?) : RecyclerView.ViewHolder(itemView) {
        val letter: ImageView = itemView.findViewById(R.id.bottom_sheet_letter)
        val title: TextView = itemView.findViewById(R.id.bottom_sheet_title)

        init {
            itemView.setOnClickListener {
                clickListener?.let {
                    it.onItemClick(this)
                }
            }
        }
    }

    interface ItemClickListener {
        fun onItemClick(viewHolder: ViewHolder)
    }

    var clickListener: ItemClickListener? = null

    fun setItemClickListener(listener: ItemClickListener) {
        clickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_bottom_sheet, parent, false)
        return ViewHolder(view, clickListener)
    }

    override fun getItemCount(): Int {
        return versions.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val version = versions.get(position)
        holder.title.text = version
        holder.letter.setImageDrawable(createLetterDrawable(version))
    }

    fun createLetterDrawable(version: String): TextDrawable {
        val letter = version.get(0).toUpperCase().toString()
        val colorGenerator = ColorGenerator.MATERIAL
        return TextDrawable.builder().buildRound(letter, colorGenerator.randomColor)
    }
}