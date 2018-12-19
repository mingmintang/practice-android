package com.mingmin.practice.firestore

import android.app.Dialog
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import com.mingmin.practice.R
import com.mingmin.practice.databinding.DialogFirestoreRestaurantsFilterBinding

class RestaurantsFilterDialog : DialogFragment() {
    interface RestaurantsFilterListener {
        fun onRestaurantsFilterConfirm(categoryId: Int, cityId: Int, priceId: Int, sortId: Int)
    }

    var categoryId: Int = 0
    var cityId: Int = 0
    var priceId: Int = 0
    var sortId: Int = 0
    lateinit var binding: DialogFirestoreRestaurantsFilterBinding
    var listener: RestaurantsFilterListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.dialog_firestore_restaurants_filter, null, false)

        if (context is RestaurantsFilterListener) {
            listener = context as RestaurantsFilterListener
        }

        arguments?.getIntArray("filters")?.let {
            categoryId = it[0]; cityId = it[1]; priceId = it[2]; sortId = it[3]
        }

        initSpinnerSelection()

        binding.firestoreFilterConfirm.setOnClickListener {
            callbackResult()
            dialog.dismiss()
        }

        binding.firestoreFilterCancel.setOnClickListener {
            dialog.dismiss()
        }

        return AlertDialog.Builder(context!!).setView(binding.root).create()
    }

    private fun initSpinnerSelection() {
        binding.firestoreCategorySpinner.setSelection(categoryId)
        binding.firestoreCitySpinner.setSelection(cityId)
        binding.firestorePriceSpinner.setSelection(priceId)
        binding.firestoreSortSpinner.setSelection(sortId)
    }

    private fun callbackResult() {
        listener?.onRestaurantsFilterConfirm(
                binding.firestoreCategorySpinner.selectedItemPosition,
                binding.firestoreCitySpinner.selectedItemPosition,
                binding.firestorePriceSpinner.selectedItemPosition,
                binding.firestoreSortSpinner.selectedItemPosition)
    }

    companion object {
        fun newInstance(categoryId: Int, cityId: Int, priceId: Int, sortId: Int): RestaurantsFilterDialog {
            val dialog = RestaurantsFilterDialog()
            val bundle = Bundle()
            bundle.putIntArray("filters", arrayOf(categoryId, cityId, priceId, sortId).toIntArray())
            dialog.arguments = bundle
            return dialog
        }
    }
}