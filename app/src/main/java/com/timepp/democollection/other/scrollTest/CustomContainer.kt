package com.timepp.democollection.other.scrollTest

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout

class CustomContainer(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    var activity: ScrollActivity? = null

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
         ev?.offsetLocation(0f, (activity!!.scrolled).toFloat())
        return super.dispatchTouchEvent(ev)
    }
}