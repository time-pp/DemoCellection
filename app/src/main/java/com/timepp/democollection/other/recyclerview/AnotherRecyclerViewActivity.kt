package com.timepp.democollection.other.recyclerview

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.timepp.democollection.R
import java.lang.Exception

class AnotherRecyclerViewActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)
        val recyclerview = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = TestAdapter()

        findViewById<View>(R.id.start_refresh).setOnClickListener {
            startRefresh(recyclerview)
            Toast.makeText(this, "start refresh", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startRefresh(recyclerView: RecyclerView) {
        Thread(Runnable {
            for (i in 1..100) {
                try {
                    Thread.sleep(3000)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
                recyclerView.post {
                    recyclerView.adapter?.notifyDataSetChanged()
                    Toast.makeText(recyclerView.context, "refreshed",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }).start()
    }

    inner class TestViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    inner class TestAdapter: RecyclerView.Adapter<TestViewHolder>() {
        override fun onBindViewHolder(viewHolder: TestViewHolder, position: Int) {
            if ((viewHolder.itemView as RecyclerView).adapter == null) {
                viewHolder.itemView.adapter = SubTestAdapter()
            } else {
                viewHolder.itemView.adapter?.notifyDataSetChanged()
            }
            if (position == 5) {
                viewHolder.itemView.setBackgroundColor(Color.RED)
            }
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TestViewHolder {
            val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_recycler_view, viewGroup, false)
            (view as RecyclerView).layoutManager = LinearLayoutManager(viewGroup.context, LinearLayoutManager.HORIZONTAL, false)
            view.adapter = SubTestAdapter()
            return TestViewHolder(view)
        }

        override fun getItemCount(): Int {
            return 20
        }
    }

    inner class SubTestViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    inner class SubTestAdapter: RecyclerView.Adapter<SubTestViewHolder>() {
        override fun onBindViewHolder(viewHolder: SubTestViewHolder, position: Int) {
            (viewHolder.itemView as TextView).text = "i am groot $position"
            viewHolder.itemView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 120)
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SubTestViewHolder {
            return SubTestViewHolder(TextView(viewGroup.context))
        }

        override fun getItemCount(): Int {
            return 10
        }
    }
}