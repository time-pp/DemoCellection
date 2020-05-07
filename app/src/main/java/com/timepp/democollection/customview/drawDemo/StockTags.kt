package com.timepp.democollection.customview.drawDemo

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class StockTags(context: Context, attrs: AttributeSet): View(context, attrs), View.OnClickListener {

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mBgRect = RectF()
    private var mTags = ArrayList<Pair<String, Int>>()
    private val mTagBgWidth = ArrayList<Float>()
    private var mContentWd = 0f
    private var mOffset = 0f
    private var mGap = 0f
    private var mInnerPad = 0f
    private var mCorner = 0f

    private var mMarquee = SimpleMarquee()

    override fun onFinishInflate() {
        super.onFinishInflate()
        mGap = 8f
        mInnerPad = 4f
        mCorner = 4f
        mPaint.textSize = 18f
        mOffset = (mPaint.fontMetrics.descent
                + mPaint.fontMetrics.ascent) / -2f
        mPaint.style = Paint.Style.FILL


        setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        setTagData(test())
    }

    private fun setTagData(tags: List<Pair<String, Int>>?) {
        mTagBgWidth.clear()
        mTags.clear()
        mMarquee.cancel()
        mContentWd = 0f
        tags.takeUnless { tags.isNullOrEmpty() }
            ?.let {
                val padTotal = mInnerPad * 2
                var bgWidth: Float
                for (tag in it) {
                    bgWidth = mPaint.measureText(tag.first) + padTotal
                    mContentWd += bgWidth
                    mTagBgWidth.add(bgWidth)
                }
                mContentWd += (it.size - 1) * mGap
                mTags.addAll(it)
                invalidate()
            }
    }

    private fun test(): List<Pair<String, Int>> {
        val p1 = Pair("融", Color.YELLOW)
        val p2 = Pair("科", Color.GREEN)
        val p3 = Pair("沪深", Color.RED)
        val p4 = Pair("CDR", Color.BLACK)
        val p5 = Pair("港", Color.BLUE)
        val p6 = Pair("通", Color.CYAN)
        val tags = ArrayList<Pair<String, Int>>()
        tags.add(p1)
        tags.add(p2)
        tags.add(p3)
        tags.add(p4)
        tags.add(p5)
        tags.add(p6)
        return tags
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawColor(Color.parseColor("#ff666666"))
        canvas?.save()
        /*if (mMarquee.onMarquee()) {
            canvas?.translate(mMarquee.delta, 0f)
            invalidate()
        }*/
        if (mMarquee.onMarquee()) {
            val dlt = mMarquee.delta
            //println("Kevin-- ${mMarquee.status} $dlt")
            canvas?.translate(dlt, 0f)
            invalidate()
        }
        var start = 0f
        for (piece in 0..1) {

        for (i in 0 until mTags.size) {
            mPaint.color = mTags[i].second
            mBgRect.set(start, 2f, start + mTagBgWidth[i], height.toFloat() - 2f)
            canvas?.drawRoundRect(mBgRect, mCorner, mCorner, mPaint)
            mPaint.color = Color.DKGRAY
            canvas?.drawText(mTags[i].first, start + mInnerPad, height / 2f + mOffset, mPaint)
            start += mTagBgWidth[i] + mGap
        }

        }
        canvas?.restore()
        if (!mMarquee.isInit) {
            mMarquee.init(width.toFloat(), mGap, mContentWd, 100f)
            if (mMarquee.start()) {
                invalidate()
            }
        }
        /*if (mMarquee.onCold()) {
            postInvalidateDelayed(SimpleMarquee.WAIT_TIME)
        } else {
            invalidate()
        }*/
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), 30)
    }

}