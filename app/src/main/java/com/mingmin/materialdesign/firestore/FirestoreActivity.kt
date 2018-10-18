package com.mingmin.materialdesign.firestore

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
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

    private fun addItems() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun signOut() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        val RC_SIGN_IN = 1
    }
}
