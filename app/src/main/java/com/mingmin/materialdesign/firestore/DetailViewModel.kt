package com.mingmin.materialdesign.firestore

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.Task

class DetailViewModel(val restaurantId: String) {

    val detailModel = DetailModel()
    val doc = ObservableField<RestaurantDoc>()
    var imageUrl = ObservableField<String>()
    val isLoading = ObservableBoolean(false)
    val isEmpty = ObservableBoolean(false)

    fun startListener(ratingAdapter: RatingAdapter? = null) {
        detailModel.startListener(restaurantId, object : DetailModel.RestaurantDataReadyListener {
            override fun onDataReady(doc: RestaurantDoc?) {
                this@DetailViewModel.doc.set(doc)
                imageUrl.set(doc?.photo)
            }
        })
        ratingAdapter?.startListening()
    }

    fun stopListener(ratingAdapter: RatingAdapter? = null) {
        detailModel.stopListener()
        ratingAdapter?.stopListening()
    }

    fun addNewRating(rating: Double, comment: String): Task<Unit> {
        return detailModel.addNewRating(restaurantId, rating, comment)
    }

    fun createRatingAdapter(): RatingAdapter {
        val options = FirestoreRecyclerOptions.Builder<RatingDoc>()
                .setQuery(detailModel.getRestaurantRatingQuery(restaurantId), RatingDoc::class.java)
                .build()
        val ratingAdapter = object : RatingAdapter(options) {
            override fun onDataChanged() {
                super.onDataChanged()
                if (itemCount == 0) {
                    isEmpty.set(true)
                } else {
                    isEmpty.set(false)
                }
            }
        }
        return ratingAdapter
    }
}
