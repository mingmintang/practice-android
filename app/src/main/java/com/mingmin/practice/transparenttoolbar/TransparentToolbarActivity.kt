package com.mingmin.practice.transparenttoolbar

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.mingmin.practice.R
import kotlinx.android.synthetic.main.activity_transparent_toolbar.*
import kotlinx.android.synthetic.main.fab.*
import kotlinx.android.synthetic.main.toolbar_transparent.*

class TransparentToolbarActivity : AppCompatActivity() {

    private lateinit var frameGrowAnim: Animation
    private lateinit var frameShrinkAnim: Animation
    private var isGrownAnim = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transparent_toolbar)

        setupStatusbarAndToolbar()
        initAnimation()
        setupFab()
    }

    fun setupStatusbarAndToolbar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = ContextCompat.getColor(this, R.color.black_trans80)

        setSupportActionBar(toolbar_transparent_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar_transparent_demoTitle.text = intent.getStringExtra("demoTitle")
    }

    fun initAnimation() {
        frameGrowAnim = AnimationUtils.loadAnimation(this, R.anim.frame_grow)
        frameShrinkAnim = AnimationUtils.loadAnimation(this, R.anim.frame_shrink)

        frameGrowAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                isGrownAnim = true
            }

            override fun onAnimationStart(animation: Animation?) {}

        })

        frameShrinkAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                isGrownAnim = false
            }

            override fun onAnimationStart(animation: Animation?) {}

        })
    }

    fun setupFab() {
        fab.setOnClickListener {
            if (isGrownAnim) {
                shrinkFrame()
            } else {
                growFrame()
            }
        }
    }

    fun growFrame() {
        transparent_toolbar_frame.startAnimation(frameGrowAnim)
    }

    fun shrinkFrame() {
        transparent_toolbar_frame.startAnimation(frameShrinkAnim)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId) {
            android.R.id.home -> supportFinishAfterTransition()
        }
        return super.onOptionsItemSelected(item)
    }
}
