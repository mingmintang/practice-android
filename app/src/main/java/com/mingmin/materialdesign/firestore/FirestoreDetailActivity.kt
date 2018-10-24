package com.mingmin.materialdesign.firestore

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.mingmin.materialdesign.R
import com.mingmin.materialdesign.databinding.ActivityFirestoreDetailBinding

class FirestoreDetailActivity : AppCompatActivity(),
        AddRatingDialogFragment.AddRatingDialogListener {
    lateinit var viewModel: DetailViewModel
    lateinit var binding: ActivityFirestoreDetailBinding
    lateinit var ratingsAdapter: RatingsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = DetailViewModel(intent.getStringExtra("restaurantId"))
        binding = DataBindingUtil.setContentView(this, R.layout.activity_firestore_detail)
        binding.viewModel = viewModel
        ratingsAdapter = viewModel.createRatingAdapter()

        with(binding.firestoreDetailRatings) {
            layoutManager = LinearLayoutManager(context)
            adapter = ratingsAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.startListener(ratingsAdapter)
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopListener(ratingsAdapter)
    }

    fun goBackToPrevious(view: View) {
        finishAfterTransition()
    }

    fun popupAddRatingDialog(view: View) {
        AddRatingDialogFragment().show(supportFragmentManager, "ReviewDialog")
    }

    override fun onAddRatingSubmit(rating: Double, comment: String) {
        viewModel.addNewRating(rating, comment).addOnSuccessListener {
            Snackbar.make(binding.firestoreDetailRatings, "新增評論成功", Snackbar.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Snackbar.make(binding.firestoreDetailRatings, "新增評論失敗", Snackbar.LENGTH_SHORT).show()
        }
    }
}
