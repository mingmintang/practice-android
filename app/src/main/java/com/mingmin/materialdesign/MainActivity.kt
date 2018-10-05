package com.mingmin.materialdesign

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
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

    override fun onItemClicked(view: View, position: Int) {
        when(position) {

        }
    }
}
