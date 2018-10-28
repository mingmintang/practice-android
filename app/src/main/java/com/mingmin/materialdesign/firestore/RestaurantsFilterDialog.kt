package com.mingmin.materialdesign.firestore

import android.app.Dialog
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import com.mingmin.materialdesign.R
import com.mingmin.materialdesign.databinding.DialogFirestoreRestaurantsFilterBinding

class RestaurantsFilterDialog : DialogFragment() {
    interface RestaurantsFilterListener {
        fun onRestaurantsFilterConfirm(categoryId: Int, cityId: Int, priceId: Int, sortId: Int)
    }

    private class Filter(var categoryId: Int = 0,
                         var cityId: Int = 0,
                         var priceId: Int = 0,
                         var sortId: Int = 0)

    lateinit var binding: DialogFirestoreRestaurantsFilterBinding
    var listener: RestaurantsFilterListener? = null
    private lateinit var filter: Filter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.dialog_firestore_restaurants_filter, null, false)

        if (context is RestaurantsFilterListener) {
            listener = context as RestaurantsFilterListener
        }

        if (!::filter.isInitialized) {
            filter = Filter()
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

    fun resetRestaurantsFilter() {
        if (!::binding.isInitialized || (filter.categoryId == 0
                && filter.cityId == 0 && filter.priceId == 0 && filter.sortId == 0)) {
            return
        }
        resetSpinnerSelection()
        callbackResult()
    }

    private fun resetSpinnerSelection() {
        binding.firestoreCategorySpinner.setSelection(0)
        binding.firestoreCitySpinner.setSelection(0)
        binding.firestorePriceSpinner.setSelection(0)
        binding.firestoreSortSpinner.setSelection(0)
    }

    private fun initSpinnerSelection() {
        binding.firestoreCategorySpinner.setSelection(filter.categoryId)
        binding.firestoreCitySpinner.setSelection(filter.cityId)
        binding.firestorePriceSpinner.setSelection(filter.priceId)
        binding.firestoreSortSpinner.setSelection(filter.sortId)
    }

    private fun callbackResult() {
        filter.apply {
            categoryId = binding.firestoreCategorySpinner.selectedItemPosition
            cityId = binding.firestoreCitySpinner.selectedItemPosition
            priceId = binding.firestorePriceSpinner.selectedItemPosition
            sortId = binding.firestoreSortSpinner.selectedItemPosition
        }
        listener?.onRestaurantsFilterConfirm(
                filter.categoryId,
                filter.cityId,
                filter.priceId,
                filter.sortId)
    }
}