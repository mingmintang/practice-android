package com.mingmin.materialdesign.firestore

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestoreException

class DetailViewModel(val restaurantId: String) {

    val detailModel = DetailModel()
    val doc = ObservableField<RestaurantDoc>()
    var imageUrl = ObservableField<String>()
    val isLoading = ObservableBoolean(false)
    val isEmpty = ObservableBoolean(false)

    fun startListener(ratingsAdapter: RatingsAdapter? = null) {
        detailModel.startListener(restaurantId, object : DetailModel.RestaurantDataReadyListener {
            override fun onDataReady(doc: RestaurantDoc?) {
                this@DetailViewModel.doc.set(doc)
                imageUrl.set(doc?.photo)
            }
        })
        ratingsAdapter?.startListening()
    }

    fun stopListener(ratingsAdapter: RatingsAdapter? = null) {
        detailModel.stopListener()
        ratingsAdapter?.stopListening()
    }

    fun addNewRating(rating: Double, comment: String): Task<Unit> {
        return detailModel.addNewRating(restaurantId, rating, comment)
    }

    fun createRatingAdapter(): RatingsAdapter {
        val options = FirestoreRecyclerOptions.Builder<RatingDoc>()
                .setQuery(detailModel.getRestaurantRatingQuery(restaurantId), RatingDoc::class.java)
                .build()
        val ratingsAdapter = object : RatingsAdapter(options) {
            override fun onDataChanged() {
                super.onDataChanged()
                if (itemCount == 0) isEmpty.set(true) else isEmpty.set(false)
            }

            override fun onError(e: FirebaseFirestoreException) {
                super.onError(e)
                isEmpty.set(true)
            }
        }
        return ratingsAdapter
    }
}
