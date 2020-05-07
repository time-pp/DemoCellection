package com.timepp.democollection.customview.recyclerviewtest

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.timepp.democollection.R

class RecyclerViewActivity : AppCompatActivity() {
    private lateinit var mRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reclerview)
        mRecyclerView = findViewById(R.id.recycler_view)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = TestAdapter()
        mRecyclerView.adapter = adapter
        mRecyclerView.postDelayed(Runnable {
            adapter.type = 1
            adapter.notifyItemRangeInserted(0, adapter.data.size)
        }, 2000)
    }

    inner class TestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class TestAdapter : RecyclerView.Adapter<TestViewHolder>() {
        val data = ArrayList<String>()
        var type = 0

        init {
            for (i in 0..10) {
                data.add("" + i)
            }
        }

        override fun getItemCount(): Int {
            return if (type == 0) 1 else data.size
        }

        override fun onBindViewHolder(viewholder: TestViewHolder, position: Int) {
                if (viewholder.itemView is TextView) {
                    viewholder.itemView.text = "type = 0"
                } else if (viewholder.itemView is LinearLayout) {
                    (viewholder.itemView.getChildAt(0) as? TextView)?.let {
                        it.text = "type = 1"
                    }
                    (viewholder.itemView.getChildAt(1) as? TextView)?.let {
                        it.text = "" + position
                    }
                }

        }

        override fun onCreateViewHolder(viewHolder: ViewGroup, itemType: Int): TestViewHolder {
            if (type == 0) {
                val tv = TextView(this@RecyclerViewActivity)
                tv.textSize = 26f
                tv.setTextColor(Color.BLACK)
                tv.layoutParams =
                    ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                return TestViewHolder(tv)
            } else {
                val linearLayout = LinearLayout(this@RecyclerViewActivity)
                val tv1 = TextView(this@RecyclerViewActivity)
                val tv2 = TextView(this@RecyclerViewActivity)
                tv1.textSize = 26f
                tv2.textSize = 26f
                tv1.setTextColor(Color.BLACK)
                tv2.setTextColor(Color.BLACK)
                tv1.layoutParams =
                    ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                tv2.layoutParams =
                    ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                linearLayout.addView(tv1)
                linearLayout.addView(tv2)
                return TestViewHolder(linearLayout)
            }
        }

        override fun getItemViewType(position: Int): Int {
            return type
        }
    }
}