package com.timepp.democollection.customview.scaleview

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.*
import android.os.Bundle
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.PopupWindow
import com.timepp.democollection.R

class ScaleActivity : Activity(), View.OnTouchListener {
    private val mIv by lazy { findViewById<ImageView>(R.id.scale_test_iv) }
    private val mPopupWindow by lazy { PopupWindow(ImageView(this), 360, 600) }
    private val scale = 2
    private val bitmap by lazy { Bitmap.createBitmap(360, 600, Bitmap.Config.RGB_565) }
    private val canvas by lazy { Canvas(bitmap) }
    private val matrix = Matrix()
    private val rect = Rect(0, 0, 360, 600)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scale)
        mIv.setOnTouchListener(this)
        mPopupWindow.isTouchable = false
        matrix.setScale(scale.toFloat(), scale.toFloat())
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        val touchX = (event?.x ?: return false).toInt()
        val touchY = event.y.toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mPopupWindow.showAtLocation(v, Gravity.NO_GRAVITY, event.rawX.toInt(), event.rawY.toInt())
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mPopupWindow.dismiss()
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val scaleStartX = when {
                    touchX - 360 / scale / 3 < 0 -> 0
                    touchX + 360 / scale / 3 * 2 > mIv.width -> mIv.width - 360 / scale
                    else -> touchX - 360 / scale / 3
                }
                val scaleStartY = when {
                    touchY - 300 / scale < 0 -> 0
                    touchY + 300 / scale > mIv.height -> mIv.height - 600 / scale
                    else -> touchY - 300 / scale
                }
                canvas.matrix = matrix
                rect.set(scaleStartX, scaleStartY, scaleStartX + 360 / scale, scaleStartY + 600 / scale)
                canvas.save()
                canvas.translate(-scaleStartX.toFloat(), -scaleStartY.toFloat())
                 canvas.clipRect(rect)
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
                mIv.draw(canvas)
                canvas.restore()
                mPopupWindow.update(event.rawX.toInt(), event.rawY.toInt(), -1, -1)
            }
        }
        val imageView = mPopupWindow.contentView as ImageView
        imageView.setImageBitmap(bitmap)
        return true
    }
}