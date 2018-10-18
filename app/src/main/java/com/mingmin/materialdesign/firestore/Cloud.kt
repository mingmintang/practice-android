package com.mingmin.materialdesign.firestore

import android.content.Context
import com.google.android.gms.tasks.Task
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
}

data class RestaurantDoc(val name: String,
                         val city: String,
                         val category: String,
                         val photo: String,
                         val price: Int,
                         val ratingNum: Int,
                         val ratingAvg: Double)