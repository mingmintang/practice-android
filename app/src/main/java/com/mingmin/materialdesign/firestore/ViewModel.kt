package com.mingmin.materialdesign.firestore

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.content.Context
import android.databinding.Observable
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestoreException
import com.mingmin.materialdesign.R
import java.util.concurrent.Executors

class ViewModel(application: Application) : AndroidViewModel(application) {
    class RestaurantsFilter(val categoryId: Int = 0,
                            val cityId: Int = 0,
                            val priceId: Int = 0,
                            val sortId: Int = 0)

    private val categories = application.resources.getStringArray(R.array.catelogies)
    private val cities = application.resources.getStringArray(R.array.cities)
    private val sortDescriptions = application.resources.getStringArray(R.array.sort_descriptions)
    private val model = Model()
    val isLoading = ObservableBoolean(false)
    val isEmpty = ObservableBoolean(true)
    val category = ObservableField<String>("")
    val sortDesc = ObservableField<String>("")
    val restaurantsAdapter = ObservableField<RestaurantsAdapter>()
    val restaurantsFilter = ObservableField<RestaurantsFilter>().apply {
        addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                sender?.let {
                    val filter = (it as ObservableField<*>).get() as RestaurantsFilter
                    category.set(categories[filter.categoryId])
                    sortDesc.set(sortDescriptions[filter.sortId])
                }
            }
        })
    }

    fun createRestaurantsAdapter(sortBy: String, filter: Model.Filter?, listener: RestaurantsAdapter.ItemClickListener?) {
        if (filter == null) {
            restaurantsFilter.set(RestaurantsFilter())
        }

        val query = model.getRestaurantsQuery(sortBy,
                model.getSortByDirection(sortBy), QUERY_LIMIT, filter)
        val options = FirestoreRecyclerOptions.Builder<RestaurantDoc>()
                        .setQuery(query, RestaurantDoc::class.java)
                        .build()
        val resAdapter = object : RestaurantsAdapter(options) {
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
        listener?.let { resAdapter.setItemClickListener(it) }
        restaurantsAdapter.set(resAdapter)
    }

    fun startListener() {
        isLoading.set(true)
        restaurantsAdapter.get()?.startListening()
    }

    fun stopListener() {
        restaurantsAdapter.get()?.stopListening()
    }

    fun updateRestaurantsAdapter(categoryId: Int, cityId: Int, priceId: Int, sortId: Int,
                                 listener: RestaurantsAdapter.ItemClickListener?) {
        stopListener()
        val filter = Model.Filter(
                if (categoryId > 0) categories[categoryId] else null,
                if (cityId > 0) cities[cityId] else null,
                if (priceId > 0) priceId else null
        )
        createRestaurantsAdapter(getSortById(sortId), filter, listener)
        startListener()
        restaurantsFilter.set(RestaurantsFilter(categoryId, cityId, priceId, sortId))
    }

    fun resetRestaurantsAdapter(listener: RestaurantsAdapter.ItemClickListener?) {
        stopListener()
        createRestaurantsAdapter(RestaurantDoc.FIELD_RATING_AVG, null, listener)
        startListener()
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