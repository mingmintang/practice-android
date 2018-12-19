package com.mingmin.practice.firestore.firestore

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.WriteBatch

object Firestore {
    val fdb: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    fun getWriteBatch(): WriteBatch {
        return fdb.batch()
    }

    fun getRestaurantCollection(): CollectionReference {
        return fdb.collection("restaurant")
    }

    fun getRestaurantDocument(restaurantId: String): DocumentReference {
        return getRestaurantCollection().document(restaurantId)
    }

    fun getRestaurantRatingCollection(restaurantId: String): CollectionReference {
        return getRestaurantDocument(restaurantId).collection("rating")
    }

    fun getRestaurantRatingDocument(restaurantId: String, ratingId: String): DocumentReference {
        return getRestaurantRatingCollection(restaurantId).document(ratingId)
    }
}