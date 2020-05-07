package com.timepp.democollection.other.scrollTest

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout

class CustomLinearLayout(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    inner class GestureListener: GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            return if (Math.abs(distanceY) > Math.abs(distanceX)) {
                when {
                    scrollY + distanceY > range -> scrollTo(0, range)
                    scrollY + distanceY < 0 -> scrollTo(0, 0)
                    else -> scrollBy(0, distanceY.toInt())
                }
                scrollView?.scrollTo(0, distanceY.toInt())
                true
            } else false
        }
    }

    var scrollView: View? = null
    var range = 0


    private val gestureDetector: GestureDetector = GestureDetector(context, GestureListener())

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0x3FFFFFFF, MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }
}