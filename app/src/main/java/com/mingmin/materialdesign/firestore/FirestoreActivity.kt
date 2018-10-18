package com.mingmin.materialdesign.firestore

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.mingmin.materialdesign.BuildConfig
import com.mingmin.materialdesign.R
import kotlinx.android.synthetic.main.activity_firestore.*

class FirestoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firestore)

        setSupportActionBar(firestore_toolbar)
    }

    override fun onStart() {
        super.onStart()

        if (!hasSignIn()) {
            startSignIn()
            return
        }
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

    override fun onStop() {
        super.onStop()
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
            R.id.menu_add_items -> addItems()
            R.id.menu_sign_out -> signOut()
        }
        return super.onOptionsItemSelected(item)
    }

    fun addItems() {
        showLoading()
        Cloud.addRandomRestaurants(this, 10).addOnCompleteListener {
            if (it.isSuccessful) {
                Snackbar.make(firestore_restaurants, "已新增10個隨機餐廳", Snackbar.LENGTH_SHORT)
                        .show()
            } else {
                Snackbar.make(firestore_restaurants, "新增隨機餐廳失敗", Snackbar.LENGTH_SHORT)
                        .show()
            }
            showList()
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
    }

    fun showList() {
        firestore_restaurants.visibility = View.VISIBLE
        firestore_loading.visibility = View.GONE
    }

    companion object {
        val RC_SIGN_IN = 1
    }
}
