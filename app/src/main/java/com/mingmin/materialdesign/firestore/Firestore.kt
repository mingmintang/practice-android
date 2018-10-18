package com.mingmin.materialdesign.firestore

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.WriteBatch

object Firestore {
    val fdb: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    fun getRestaurantCollection(): CollectionReference {
        return fdb.collection("restaurant")
    }

    fun getRestaurantDocument(id: String): DocumentReference {
        return getRestaurantCollection().document(id)
    }

    fun getWriteBatch(): WriteBatch {
        return fdb.batch()
    }
}