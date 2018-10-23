package com.mingmin.materialdesign.firestore

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

class DetailModel {
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
            val doc = RatingDoc(uid, userName, rating, comment)
            Firestore.getRestaurantRatingCollection(restaurantId).add(doc).addOnSuccessListener {
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
        const val TAG = "DetailModel"
    }
}