package com.timepp.democollection.other.scrollTest

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView

class CustomScrollView(context: Context?, attrs: AttributeSet?) : ScrollView(context, attrs) {
    var onScrollListener: OnScrollListener? = null

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        onScrollListener?.onScroll(this, l, t, oldl, oldt)
    }

    interface OnScrollListener {
        fun onScroll(view: View, l: Int, t: Int, oldl: Int, oldt: Int)
    }

}