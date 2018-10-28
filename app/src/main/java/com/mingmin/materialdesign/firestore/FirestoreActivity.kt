package com.mingmin.materialdesign.firestore

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.mingmin.materialdesign.BuildConfig
import com.mingmin.materialdesign.R
import com.mingmin.materialdesign.databinding.ActivityFirestoreBinding
import com.mingmin.materialdesign.firestore.ViewModel.Companion.DEFAULT_COUNT
import kotlinx.android.synthetic.main.activity_firestore.*

class FirestoreActivity : AppCompatActivity(), RestaurantsAdapter.ItemClickListener,
        RestaurantsFilterDialog.RestaurantsFilterListener {
    lateinit var viewModel: ViewModel
    lateinit var binding: ActivityFirestoreBinding
    lateinit var restaurantsAdapter: RestaurantsAdapter
    lateinit var filterDialog: RestaurantsFilterDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModel(application)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_firestore)
        binding.viewModel = viewModel

        setSupportActionBar(firestore_toolbar)
        initRecyclerView()

        filterDialog = RestaurantsFilterDialog()
    }

    fun initRecyclerView() {
        restaurantsAdapter = viewModel.createRestaurantsAdapter(RestaurantDoc.FIELD_RATING_AVG, null)
        restaurantsAdapter.setItemClickListener(this)
        binding.firestoreRestaurants.layoutManager = LinearLayoutManager(this)
        binding.firestoreRestaurants.adapter = restaurantsAdapter
    }

    override fun onStart() {
        super.onStart()

        if (!hasSignIn()) {
            startSignIn()
            return
        }

        viewModel.startListener(restaurantsAdapter)
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
        viewModel.stopListener(restaurantsAdapter)
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
        viewModel.addRandomRestaurants(this, DEFAULT_COUNT).addOnCompleteListener {
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
        viewModel.clearRestaurants(DEFAULT_COUNT).addOnCompleteListener {
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
        AuthUI.getInstance().signOut(this).addOnCompleteListener {
            startSignIn()
        }
    }

    override fun onRestaurantsItemClick(restaurantId: String) {
        startActivity(Intent(this, FirestoreDetailActivity::class.java).apply {
            putExtra("restaurantId", restaurantId)
        })
    }

    fun popupRestaurantsFilterDialog(view: View) {
        filterDialog.show(supportFragmentManager, "RestaurantsFilter")
    }

    override fun onRestaurantsFilterConfirm(categoryId: Int, cityId: Int, priceId: Int, sortId: Int) {
        restaurantsAdapter = viewModel.updateRestaurantsAdapter(restaurantsAdapter,
                categoryId, cityId, priceId, sortId,
                this)
        binding.firestoreRestaurants.swapAdapter(restaurantsAdapter, false)
    }

    fun resetRestaurantsFilter(view: View) {
        filterDialog.resetRestaurantsFilter()
    }

    companion object {
        const val RC_SIGN_IN = 1
    }
}
