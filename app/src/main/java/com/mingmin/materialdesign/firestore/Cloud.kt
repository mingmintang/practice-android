package com.mingmin.materialdesign.firestore

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ServerTimestamp
import com.mingmin.materialdesign.R
import java.util.*

object Cloud {
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
            val doc = RestaurantDoc(name, city, category, photoUrl,price, ratingNum, ratingAvg)
            batch.set(Firestore.getRestaurantDocument(col.document().id), doc)
        }

        return batch.commit()
    }

    fun clearRestaurants(count: Int): Task<Int> {
        val source = TaskCompletionSource<Int>()
        val batch = Firestore.getWriteBatch()

        getRestaurantQuery(RestaurantDoc.FIELD_RATING_AVG, Query.Direction.DESCENDING, count)
                .get().addOnCompleteListener {
                    var resultCount = 0
                    if (it.isSuccessful) {
                        it.result?.iterator()?.forEach { doc ->
                            batch.delete(Firestore.getRestaurantDocument(doc.id))
                            resultCount += 1
                        }
                    }

                    if (resultCount > 0) {
                        batch.commit().addOnSuccessListener { void ->
                            source.setResult(resultCount)
                        }.addOnFailureListener { exception ->
                            source.setException(exception)
                        }
                    } else {
                        source.setException(Exception("Result count is empty."))
                    }

                }
        return source.task
    }

    fun getRestaurantQuery(orderBy: String,
                 orderDerection: Query.Direction, limit: Int): Query {
        return Firestore.getRestaurantCollection()
                .orderBy(orderBy, orderDerection)
                .limit(limit.toLong())
    }
}

data class RestaurantDoc(val name: String = "",
                         val city: String = "",
                         val category: String = "",
                         val photo: String = "",
                         val price: Int = 1,
                         val ratingNum: Int = 0,
                         val ratingAvg: Double = 0.0) {
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