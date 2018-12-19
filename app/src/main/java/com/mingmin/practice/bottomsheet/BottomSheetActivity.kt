package com.mingmin.practice.bottomsheet

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.mingmin.practice.Demo
import com.mingmin.practice.R
import kotlinx.android.synthetic.main.activity_bottom_sheet.*
import kotlinx.android.synthetic.main.sheet_bottom_sheet.*

class BottomSheetActivity : AppCompatActivity(), BottomSheetAdapter.ItemClickListener {

    lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    lateinit var fabGrowAnim: Animation
    lateinit var fabShrinkAnim: Animation
    var showBottomSheet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_sheet)

        setSupportActionBar(bottom_sheet_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        bottom_sheet_demoTitle.text = intent.getStringExtra("demoTitle")

        setupRecyclerView()
        initAnimation()
        setupBottomSheet()
    }

    fun setupRecyclerView() {
        val adapter = BottomSheetAdapter(Demo.getVersionList())
        adapter.setItemClickListener(this)
        bottom_sheet_recyclerView.layoutManager = LinearLayoutManager(this)
        bottom_sheet_recyclerView.itemAnimator = DefaultItemAnimator()
        bottom_sheet_recyclerView.adapter = adapter
        bottom_sheet_recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                } else if (showBottomSheet && dy < 0
                        && bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }
        })
    }

    fun initAnimation() {
        fabGrowAnim = AnimationUtils.loadAnimation(this, R.anim.fab_grow)
        fabShrinkAnim = AnimationUtils.loadAnimation(this, R.anim.fab_shrink)
            fabShrinkAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                (bottom_sheet_fab as View).visibility = View.GONE
            }

            override fun onAnimationStart(animation: Animation?) {}
        })
    }

    fun setupBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {}

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> showFab()
                    BottomSheetBehavior.STATE_HIDDEN -> hideFab()
                }
            }
        })
    }

    fun showFab() {
        val view = bottom_sheet_fab as View
        if (view.visibility != View.VISIBLE) {
            view.visibility = View.VISIBLE
            bottom_sheet_fab.startAnimation(fabGrowAnim)
        }

    }

    fun hideFab() {
        bottom_sheet_fab.startAnimation(fabShrinkAnim)
    }

    override fun onItemClick(viewHolder: BottomSheetAdapter.ViewHolder) {
        bottom_sheet_versionName.text = viewHolder.title.text
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
            if (!showBottomSheet) showBottomSheet = true
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId) {
            android.R.id.home -> supportFinishAfterTransition()
        }
        return super.onOptionsItemSelected(item)
    }
}
