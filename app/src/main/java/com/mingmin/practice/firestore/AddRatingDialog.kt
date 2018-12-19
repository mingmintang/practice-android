package com.mingmin.practice.firestore

import android.app.Dialog
import android.os.Bundle
import android.support.design.button.MaterialButton
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.widget.EditText
import com.mingmin.practice.R
import me.zhanghai.android.materialratingbar.MaterialRatingBar

class AddRatingDialog : DialogFragment() {
    interface AddRatingDialogListener {
        fun onAddRatingSubmit(rating: Double, comment: String)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context)
                .inflate(R.layout.dialog_firestore_add_rating, null)

        var listener: AddRatingDialogListener? = null
        if (context is AddRatingDialogListener) {
            listener = context as AddRatingDialogListener
        }

        view.findViewById<MaterialButton>(R.id.firestore_add_rating_submit).setOnClickListener {
            val rating = view.findViewById<MaterialRatingBar>(R.id.firestore_add_rating_rating)
                    .rating.toDouble()
            val comment = view.findViewById<EditText>(R.id.firestore_add_rating_comment)
                    .text.toString()
             listener?.onAddRatingSubmit(rating, comment)
            dismiss()
        }

        view.findViewById<MaterialButton>(R.id.firestore_add_rating_cancel).setOnClickListener {
            dismiss()
        }

        return AlertDialog.Builder(context!!).setView(view).create()
    }
}