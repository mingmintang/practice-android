package com.mingmin.practice.ratingbar

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.chip.Chip
import android.support.design.chip.ChipGroup
import android.support.design.widget.BottomSheetBehavior
import android.view.MenuItem
import android.view.View
import com.mingmin.practice.R
import kotlinx.android.synthetic.main.activity_rating_bar.*
import kotlinx.android.synthetic.main.sheet_ratingbar.*
import kotlinx.android.synthetic.main.toolbar.*

class RatingBarActivity : AppCompatActivity() {
    lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    var chipGroup1Id = R.id.ratingbar_chip1_1
    var chipGroup2Id = R.id.ratingbar_chip2_1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating_bar)

        setupToolbar()
        setupBottomSheet()
        setupCardView()
    }

    fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar_demoTitle.text = intent.getStringExtra("demoTitle")
    }

    fun setupBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(ratingbar_bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        ratingbar_chipGroup1.check(chipGroup1Id)
        ratingbar_chipGroup1.setOnCheckedChangeListener { chipGroup, checkedId ->
            if (checkedId == ChipGroup.NO_ID) {
                val chip: Chip = this.findViewById(chipGroup1Id)
                chip.isChecked = true
            } else {
                chipGroup1Id = checkedId
            }
        }

        ratingbar_chipGroup2.check(chipGroup2Id)
        ratingbar_chipGroup2.setOnCheckedChangeListener { chipGroup, checkedId ->
            if (checkedId == ChipGroup.NO_ID) {
                val chip: Chip = this.findViewById(chipGroup2Id)
                chip.isChecked = true
            } else {
                chipGroup2Id = checkedId
            }
        }
    }

    fun setupCardView() {

    }

    fun onCardViewClick(view: View) {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    fun bottomSheetSave(veiw: View) {
        var score = 0
        when(chipGroup1Id) {
            R.id.ratingbar_chip1_1 -> score += 0
            R.id.ratingbar_chip1_2 -> score += 2
            R.id.ratingbar_chip1_3 -> score += 6
            R.id.ratingbar_chip1_4 -> score += 18
        }
        when(chipGroup2Id) {
            R.id.ratingbar_chip2_1 -> score += 0
            R.id.ratingbar_chip2_2 -> score += 5
            R.id.ratingbar_chip2_3 -> score += 10
            R.id.ratingbar_chip2_4 -> score += 20
        }
        when(score) {
            0 -> ratingbar_ratingBar.rating = 0f
            in 1..4 -> ratingbar_ratingBar.rating = 0.5f
            in 5..8 -> ratingbar_ratingBar.rating = 1f
            in 9..12 -> ratingbar_ratingBar.rating = 1.5f
            in 13..16 -> ratingbar_ratingBar.rating = 2f
            in 17..20 -> ratingbar_ratingBar.rating = 2.5f
            in 21..24 -> ratingbar_ratingBar.rating = 3f
            in 25..28 -> ratingbar_ratingBar.rating = 3.5f
            in 29..32 -> ratingbar_ratingBar.rating = 4f
            in 33..37 -> ratingbar_ratingBar.rating = 4.5f
            38 -> ratingbar_ratingBar.rating = 5f
        }
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    fun bottomSheetCancel(veiw: View) {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId) {
            android.R.id.home -> supportFinishAfterTransition()
        }
        return super.onOptionsItemSelected(item)
    }
}
