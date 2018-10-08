package com.mingmin.materialdesign

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.mingmin.materialdesign.bottomsheet.BottomSheetActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainRecyclerViewAdapter.OnItemClickListener {

    private lateinit var adapter: MainRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val demos: ArrayList<Demo> = Demo.getDemoList()
        adapter = MainRecyclerViewAdapter(demos)
        adapter.setOnItemClickListener(this)

        main_recyclerView.setHasFixedSize(true)
        main_recyclerView.layoutManager = LinearLayoutManager(this)
        main_recyclerView.adapter = adapter
    }

    override fun onItemClicked(viewHolder: MainRecyclerViewAdapter.ViewHolder, position: Int) {
        val intent: Intent?
        when(position) {
            0 -> intent = Intent(this, BottomSheetActivity::class.java)
            else -> intent = null
        }
        intent?.let {
            intent.putExtra("demoTitle", viewHolder.title.text.toString())
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this, viewHolder.title,
                            resources.getString(R.string.transitionName_demoTitle)).toBundle()
            startActivity(intent, options)
        }
    }
}
