package com.mingmin.materialdesign.firestore

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.mingmin.materialdesign.BuildConfig
import com.mingmin.materialdesign.R
import kotlinx.android.synthetic.main.activity_firestore.*

class FirestoreActivity : AppCompatActivity(), FirestoreAdapter.ItemClickListener {
    lateinit var mAdapter: FirestoreAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firestore)
        setSupportActionBar(firestore_toolbar)

        initRecyclerView()
    }

    override fun onStart() {
        super.onStart()

        if (!hasSignIn()) {
            startSignIn()
            return
        }

        showLoading()
        mAdapter.startListening()
    }

    fun hasSignIn(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    fun startSignIn() {
        val email = AuthUI.IdpConfig.EmailBuilder()
                .setAllowNewAccounts(false)
                .build()
        val intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(listOf(email))
                .setIsSmartLockEnabled(!BuildConfig.DEBUG, true)
                .build()
        startActivityForResult(intent, RC_SIGN_IN)
    }

    fun initRecyclerView() {
        val query = Cloud.getRestaurantQuery(RestaurantDoc.FIELD_RATING_AVG,
                Query.Direction.DESCENDING, QUERY_LIMIT)
        val options =
                FirestoreRecyclerOptions.Builder<RestaurantDoc>()
                        .setQuery(query, RestaurantDoc::class.java)
                        .build()
        mAdapter = object : FirestoreAdapter(options) {
            override fun onDataChanged() {
                super.onDataChanged()
                if (itemCount == 0) showEmpty() else showList()
            }

            override fun onError(e: FirebaseFirestoreException) {
                super.onError(e)
                showEmpty()
            }
        }.also {
            it.setItemClickListener(this)
        }
        firestore_restaurants.layoutManager = LinearLayoutManager(this)
        firestore_restaurants.adapter = mAdapter
    }

    override fun onStop() {
        super.onStop()
        mAdapter.stopListening()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            RC_SIGN_IN -> {
                if (resultCode != Activity.RESULT_OK && !hasSignIn()) {
                    startSignIn()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_firestore, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId) {
            R.id.menu_add_items -> addRandomItems()
            R.id.menu_clear_items -> clearItems()
            R.id.menu_sign_out -> signOut()
        }
        return super.onOptionsItemSelected(item)
    }

    fun addRandomItems() {
        showLoading()
        Cloud.addRandomRestaurants(this, DEFAULT_COUNT).addOnCompleteListener {

            if (it.isSuccessful) {
                Snackbar.make(firestore_restaurants, "已新增${DEFAULT_COUNT}個隨機餐廳", Snackbar.LENGTH_SHORT)
                        .show()
            } else {
                Snackbar.make(firestore_restaurants, "新增隨機餐廳失敗", Snackbar.LENGTH_SHORT)
                        .show()
            }
        }
    }

    fun clearItems() {
        showLoading()
        Cloud.clearRestaurants(DEFAULT_COUNT).addOnCompleteListener {
            if (it.isSuccessful) {
                Snackbar.make(firestore_restaurants, "已刪除${it.result}個餐廳", Snackbar.LENGTH_SHORT)
                        .show()
            } else {
                Snackbar.make(firestore_restaurants, "沒有可刪除的餐廳", Snackbar.LENGTH_SHORT)
                        .show()
            }
        }
    }

    fun signOut() {
        showLoading()
        AuthUI.getInstance().signOut(this).addOnCompleteListener {
            startSignIn()
        }
    }

    fun showLoading() {
        firestore_restaurants.visibility = View.GONE
        firestore_loading.visibility = View.VISIBLE
        firestore_empty_result.visibility = View.GONE
    }

    fun showEmpty() {
        firestore_restaurants.visibility = View.GONE
        firestore_loading.visibility = View.GONE
        firestore_empty_result.visibility = View.VISIBLE
    }

    fun showList() {
        firestore_restaurants.visibility = View.VISIBLE
        firestore_loading.visibility = View.GONE
        firestore_empty_result.visibility = View.GONE
    }

    override fun onItemClick(restaurantId: String) {
        startActivity(Intent(this, FirestoreDetailActivity::class.java).apply {
            putExtra("restaurantId", restaurantId)
        })
    }

    companion object {
        const val RC_SIGN_IN = 1
        const val QUERY_LIMIT = 50
        const val DEFAULT_COUNT = 20
    }
}
