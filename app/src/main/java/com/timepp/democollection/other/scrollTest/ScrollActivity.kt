package com.timepp.democollection.other.scrollTest

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.timepp.democollection.R

class ScrollActivity: AppCompatActivity() {

    /*private val container by lazy { findViewById<CustomLinearLayout>(R.id.container) }
    private val scrollView by lazy { findViewById<View>(R.id.sub_view) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll)
        container.scrollView = scrollView
        container.range = 150
    }*/


    private val fixedView by lazy { findViewById<View>(R.id.fixed_view) }
    private val hideView by lazy { findViewById<View>(R.id.hide_view) }
    private val scrollView by lazy { findViewById<CustomScrollView>(R.id.scroll_view) }
    private val scrollViewContainer by lazy { findViewById<CustomContainer>(R.id.scroll_view_container) }
    var scrolled = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frame_scroll)
        val view = findViewById<View>(R.id.scroll_view_container)
        val layoutParams = view.layoutParams
        fixedView.setOnClickListener { Toast.makeText(this@ScrollActivity, "fixed", Toast.LENGTH_SHORT).show()}
        hideView.setOnClickListener { Toast.makeText(this@ScrollActivity, "hided", Toast.LENGTH_SHORT).show()}
        layoutParams.height = 580 * 3
        view.layoutParams = layoutParams
        scrollViewContainer.activity = this
        scrollView.onScrollListener = object : CustomScrollView.OnScrollListener {
            override fun onScroll(view: View, l: Int, t: Int, oldl: Int, oldt: Int) {
                val curScroll = t - oldt
                val realScroll: Int =
                when {
                    scrolled - curScroll < - 120 -> scrolled + 120
                    scrolled - curScroll > 0 -> scrolled
                    else -> curScroll
                }
                /*if (t < oldt) {
                    Log.d(TAG, "t < oldt")
                } else {
                    Log.d(TAG, "t >= oldt")
                }*/
                scrolled -= realScroll
                hideView.layout(hideView.left, hideView.top - realScroll, hideView.right, hideView.bottom - realScroll)
                fixedView.layout(fixedView.left, fixedView.top - realScroll, fixedView.right, fixedView.bottom - realScroll)
                scrollViewContainer.layout(scrollViewContainer.left, scrollViewContainer.top - realScroll, scrollViewContainer.right, scrollViewContainer.bottom)
            }
        }
    }
}

const val TAG = "ScrollActivity"
