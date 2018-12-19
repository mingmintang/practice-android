package com.mingmin.practice.firestore.firestore

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class RestaurantDetail {
    interface RestaurantDataReadyListener {
        fun onDataReady(doc: RestaurantDoc?)
    }

    var mRestaurantRegistration: ListenerRegistration? = null

    fun startListener(restaurantId: String, listener: RestaurantDataReadyListener) {
        mRestaurantRegistration = Firestore.getRestaurantDocument(restaurantId)
                .addSnapshotListener(EventListener<DocumentSnapshot> { snapshot, e ->
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e)
                        return@EventListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        val doc = snapshot.toObject(RestaurantDoc::class.java)
                        listener.onDataReady(doc)
                    } else {
                        Log.w(TAG, "snapshot is not existed.")
                    }
                })
    }

    fun stopListener() {
        if (mRestaurantRegistration != null) {
            mRestaurantRegistration!!.remove()
            mRestaurantRegistration = null
        }
    }

    fun addNewRating(restaurantId: String, rating: Double, comment: String): Task<Unit> {
        val source = TaskCompletionSource<Unit>()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val uid = user.uid
            val userName = if (user.displayName != null) user.displayName!! else ""
            val ratingDoc = RatingDoc(uid, userName, rating, comment)
            val restaurantRef = Firestore.getRestaurantDocument(restaurantId)
            val ratingRef = Firestore.getRestaurantRatingCollection(restaurantId).document()

            Firestore.fdb.runTransaction { transaction ->
                val restaurantDoc = transaction.get(restaurantRef).toObject(RestaurantDoc::class.java)
                restaurantDoc?.let { doc ->
                    val newRatingNum = doc.ratingNum + 1
                    val oldRatingTotal = doc.ratingAvg * doc.ratingNum
                    val newRatingAvg = (oldRatingTotal + rating) / newRatingNum
                    doc.ratingNum = newRatingNum
                    doc.ratingAvg = newRatingAvg

                    transaction.set(restaurantRef, doc)
                    transaction.set(ratingRef, ratingDoc)
                }
                null
            }.addOnSuccessListener {
                source.setResult(null)
            }.addOnFailureListener {
                source.setException(it)
            }
        } else {
            source.setException(Exception("There is not login"))
        }

        return source.task
    }

    fun getRestaurantRatingQuery(restaurantId: String): Query {
        return Firestore.getRestaurantRatingCollection(restaurantId)
                .orderBy(RatingDoc.FIELD_TIMESTAMP, Query.Direction.DESCENDING)
                .limit(50)
    }

    companion object {
        const val TAG = "RestaurantDetail"
    }
}