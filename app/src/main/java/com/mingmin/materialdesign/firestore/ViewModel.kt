package com.mingmin.materialdesign.firestore

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.content.Context
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestoreException
import com.mingmin.materialdesign.R
import java.util.concurrent.Executors

class ViewModel(application: Application) : AndroidViewModel(application) {
    private val categories = application.resources.getStringArray(R.array.catelogies)
    private val cities = application.resources.getStringArray(R.array.cities)
    private val sortDescriptions = application.resources.getStringArray(R.array.sort_descriptions)
    private val model = Model()
    val isLoading = ObservableBoolean(false)
    val isEmpty = ObservableBoolean(true)
    val category = ObservableField<String>("")
    val sortDesc = ObservableField<String>("")

    fun createRestaurantsAdapter(sortBy: String, filter: Model.Filter?): RestaurantsAdapter {
        if (filter == null) {
            category.set(categories[0])
            sortDesc.set(sortDescriptions[0])
        }

        val query = model.getRestaurantsQuery(sortBy,
                model.getSortByDirection(sortBy), QUERY_LIMIT, filter)
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

    fun updateRestaurantsAdapter(oldAdapter: RestaurantsAdapter,
                                 categoryId: Int, cityId: Int, priceId: Int, sortId: Int,
                                 listener: RestaurantsAdapter.ItemClickListener?): RestaurantsAdapter {
        stopListener(oldAdapter)
        val filter = Model.Filter(
                if (categoryId > 0) categories[categoryId] else null,
                if (cityId > 0) cities[cityId] else null,
                if (priceId > 0) priceId else null
        )
        val adapter = createRestaurantsAdapter(getSortById(sortId), filter)
        listener?.let { adapter.setItemClickListener(it) }
        startListener(adapter)
        category.set(categories[categoryId])
        sortDesc.set(sortDescriptions[sortId])

        return adapter
    }

    private fun getSortById(sortId: Int): String {
        var sortBy: String = RestaurantDoc.FIELD_RATING_AVG
        when(sortId) {
            0 -> sortBy = RestaurantDoc.FIELD_RATING_AVG
            1 -> sortBy = RestaurantDoc.FIELD_RATING_NUM
            2 -> sortBy = RestaurantDoc.FIELD_PRICE
        }
        return sortBy
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