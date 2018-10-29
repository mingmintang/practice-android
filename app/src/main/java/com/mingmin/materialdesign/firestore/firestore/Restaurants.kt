package com.mingmin.materialdesign.firestore.firestore

import android.content.Context
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ServerTimestamp
import com.mingmin.materialdesign.R
import java.util.*
import java.util.concurrent.Executor

class Restaurants {
    class Filter(val category: String?, val city: String?, val price: Int?)

    fun getSortByDirection(sortBy: String): Query.Direction {
        var direction = Query.Direction.ASCENDING
        when(sortBy) {
            RestaurantDoc.FIELD_RATING_AVG -> direction = Query.Direction.DESCENDING
            RestaurantDoc.FIELD_RATING_NUM -> direction = Query.Direction.DESCENDING
            RestaurantDoc.FIELD_PRICE -> direction = Query.Direction.ASCENDING
        }
        return direction
    }

    fun getRestaurantsQuery(orderBy: String, orderDirection: Query.Direction,
                            limit: Int, filter: Filter?): Query {
        var query = Firestore.getRestaurantCollection().limit(limit.toLong())
        filter?.let { f ->
            f.category?.let { query = query.whereEqualTo(RestaurantDoc.FIELD_CATEGORY, it) }
            f.city?.let { query = query.whereEqualTo(RestaurantDoc.FIELD_CITY, it) }
            f.price?.let { query = query.whereEqualTo(RestaurantDoc.FIELD_PRICE, it) }
        }
        if (filter?.price == null || orderBy != RestaurantDoc.FIELD_PRICE) {
            query = query.orderBy(orderBy, orderDirection)
        }

        return query
    }

    fun addRandomRestaurants(context: Context, count: Int): Task<Void> {
        val col = Firestore.getRestaurantCollection()
        val batch = Firestore.getWriteBatch()
        val PHOTO_URL = "https://storage.googleapis.com/firestorequickstarts.appspot.com/food_%d.png"
        val MAX_PHOTO_NUM = 22
        val firstNames = context.resources.getStringArray(R.array.restaurant_first_names)
        val lastNames = context.resources.getStringArray(R.array.restaurant_last_names)
        val cities = context.resources.getStringArray(R.array.cities).let {
            it.copyOfRange(1, it.size)
        }
        val categories = context.resources.getStringArray(R.array.catelogies).let {
            it.copyOfRange(1, it.size)
        }
        val prices = arrayOf(1, 2, 3)

        val random = Random()
        for (i in 1..count) {
            val name = firstNames.get(random.nextInt(firstNames.size)) + lastNames.get(random.nextInt(lastNames.size))
            val city = cities.get(random.nextInt(cities.size))
            val category = categories.get(random.nextInt(categories.size))
            val photoUrl = String.format(Locale.getDefault(), PHOTO_URL, random.nextInt(MAX_PHOTO_NUM) + 1)
            val price = prices.get(random.nextInt(prices.size))
            val ratingNum = random.nextInt(100)
            val ratingAvg = random.nextDouble() * 5
            val doc = RestaurantDoc(name, city, category, photoUrl, price, ratingNum, ratingAvg)
            batch.set(Firestore.getRestaurantDocument(col.document().id), doc)
        }

        return batch.commit()
    }

    fun clearRestaurants(exe: Executor, count: Int): Task<Int> {
        val source = TaskCompletionSource<Int>()
        val batch = Firestore.getWriteBatch()
        getRestaurantsQuery(RestaurantDoc.FIELD_RATING_AVG, Query.Direction.DESCENDING, count, null)
                .get().addOnCompleteListener(exe, OnCompleteListener<QuerySnapshot> { task ->
                    var restaurantCount = 0
                    if (task.isSuccessful) {
                        task.result?.iterator()?.forEach { snap ->
                            val restaurantId = snap.id
                            batch.delete(Firestore.getRestaurantDocument(restaurantId))
                            restaurantCount += 1
                            val subTask = Firestore.getRestaurantRatingCollection(restaurantId).get()
                                    .addOnCompleteListener { subTask ->
                                        if (subTask.isSuccessful) {
                                            subTask.result?.iterator()?.forEach { subSnap ->
                                                batch.delete(Firestore.getRestaurantRatingDocument(restaurantId, subSnap.id))
                                            }
                                        }
                                    }
                            Tasks.await(subTask)
                        }
                    }

                    if (restaurantCount > 0) {
                        batch.commit().addOnSuccessListener { void ->
                            source.setResult(restaurantCount)
                        }.addOnFailureListener { exception ->
                            source.setException(exception)
                        }
                    } else {
                        source.setException(Exception("Restaurant count is empty."))
                    }
                })
        return source.task
    }
}

data class RestaurantDoc(val name: String = "",
                         val city: String = "",
                         val category: String = "",
                         val photo: String = "",
                         val price: Int = 1,
                         var ratingNum: Int = 0,
                         var ratingAvg: Double = 0.0) {
    companion object {
        val FIELD_NAME = "name"
        val FIELD_CITY = "city"
        val FIELD_CATEGORY = "category"
        val FIELD_PHOTO = "photo"
        val FIELD_PRICE = "price"
        val FIELD_RATING_NUM = "ratingNum"
        val FIELD_RATING_AVG = "ratingAvg"
    }
}

data class RatingDoc(val userId: String = "",
                     val userName: String = "",
                     val rating: Double = 0.0,
                     val comment: String = "",
                     @ServerTimestamp val timestamp: Date? = null) {
    companion object {
        val FIELD_TIMESTAMP = "timestamp"
    }
}