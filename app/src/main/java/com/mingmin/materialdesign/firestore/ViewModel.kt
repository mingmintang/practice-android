package com.mingmin.materialdesign.firestore

import android.content.Context
import android.databinding.ObservableBoolean
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import java.util.concurrent.Executors

class ViewModel {
    val model = Model()
    var isLoading = ObservableBoolean(false)
    var isEmpty = ObservableBoolean(true)

    fun createRestaurantsAdapter(): RestaurantsAdapter {
        val query = model.getRestaurantsQuery(RestaurantDoc.FIELD_RATING_AVG,
                Query.Direction.DESCENDING, QUERY_LIMIT)
        val options = FirestoreRecyclerOptions.Builder<RestaurantDoc>()
                        .setQuery(query, RestaurantDoc::class.java)
                        .build()
        return object : RestaurantsAdapter(options) {
            override fun onDataChanged() {
                super.onDataChanged()
                isLoading.set(false)
                isEmpty.set(itemCount == 0)
            }

            override fun onError(e: FirebaseFirestoreException) {
                super.onError(e)
                isLoading.set(false)
                isEmpty.set(true)
            }
        }
    }

    fun startListener(adapter: RestaurantsAdapter) {
        isLoading.set(true)
        adapter.startListening()
    }

    fun stopListener(adapter: RestaurantsAdapter) {
        adapter.stopListening()
    }

    fun addRandomRestaurants(context: Context, count: Int): Task<Void> {
        isLoading.set(true)
        return model.addRandomRestaurants(context, count)
    }

    fun clearRestaurants(count: Int): Task<Int> {
        isLoading.set(true)
        val exe = Executors.newSingleThreadExecutor()
        return model.clearRestaurants(exe, count).addOnFailureListener {
            isLoading.set(false)
            isEmpty.set(true)
        }
    }

    companion object {
        const val QUERY_LIMIT = 50
        const val DEFAULT_COUNT = 20
    }
}