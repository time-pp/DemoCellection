package com.timepp.democollection.customview.drawDemo

import android.graphics.*
import android.graphics.drawable.Drawable
import java.lang.IllegalArgumentException

class ShadowDrawable(private val shadowWidth: Int, private val shadowRadius: Int) : Drawable() {

    companion object {
        private const val LEFT = 0
        private const val LEFT_TOP = 1
        private const val TOP = 2
        private const val RIGHT_TOP = 3
        private const val RIGHT = 4
        private const val RIGHT_BOTTOM = 5
        private const val BOTTOM = 6
        private const val LEFT_BOTTOM = 7
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var shadowColor = 0
    private var shadowEndColor = 0
    private var innerColor = 0
    private val shadows = ArrayList<Shadow>()
    private val radius: Int
    init {
        if (shadowWidth < 0 || shadowRadius < 0) {
            throw IllegalArgumentException(
                "shadowWidth and shadowRadius must > 0, shadowWidth = $shadowWidth, shadowRadius = $shadowRadius")
        }
        radius = shadowWidth + shadowRadius
    }
    fun setColor(shadowColor: Int, innerColor: Int) {
        this.shadowColor = shadowColor
        shadowEndColor = shadowColor and 0x00FFFFFF
        this.innerColor = innerColor
        updateGradient()
        invalidateSelf()
    }

    override fun onBoundsChange(bounds: Rect?) {
        super.onBoundsChange(bounds)
        update()
    }

    private fun update() {
        val rect = bounds
        if (rect.isEmpty) {
            return
        }
        if (shadowRadius == 0) {
            buildNoRadiusShadow()
        } else {
            buildRadiusShadow()
        }
        invalidateSelf()
    }

    private fun buildNoRadiusShadow() {
        shadows.clear()
        for (direction in LEFT..LEFT_BOTTOM) {
            shadows.add(RectShadow(getRect(direction), getLinearGradient(direction), direction))
        }
    }

    private fun getLinearGradient(direction: Int): LinearGradient {
        return when (direction) {
            LEFT -> LinearGradient((bounds.left + shadowWidth).toFloat(), bounds.top.toFloat(), bounds.left.toFloat(),
                bounds.top.toFloat(), shadowColor, shadowEndColor, Shader.TileMode.CLAMP)
            LEFT_TOP -> LinearGradient((bounds.left + shadowWidth).toFloat(), (bounds.top + shadowWidth).toFloat(), bounds.left.toFloat(),
                bounds.top.toFloat(), shadowColor, shadowEndColor, Shader.TileMode.CLAMP)
            TOP -> LinearGradient(bounds.left.toFloat(), (bounds.top + shadowWidth).toFloat(), bounds.left.toFloat(),
                bounds.top.toFloat(), shadowColor, shadowEndColor, Shader.TileMode.CLAMP)
            RIGHT_TOP -> LinearGradient((bounds.right - shadowWidth).toFloat(), (bounds.top + shadowWidth).toFloat(), bounds.right.toFloat(),
                bounds.top.toFloat(), shadowColor, shadowEndColor, Shader.TileMode.CLAMP)
            RIGHT -> LinearGradient((bounds.right - shadowWidth).toFloat(), bounds.top.toFloat(), bounds.right.toFloat(),
                bounds.top.toFloat(), shadowColor, shadowEndColor, Shader.TileMode.CLAMP)
            RIGHT_BOTTOM -> LinearGradient((bounds.right - shadowWidth).toFloat(), (bounds.bottom - shadowWidth).toFloat(), bounds.right.toFloat(),
                bounds.bottom.toFloat(), shadowColor, shadowEndColor, Shader.TileMode.CLAMP)
            BOTTOM -> LinearGradient(bounds.left.toFloat(), (bounds.bottom - shadowWidth).toFloat(),
                bounds.left.toFloat(), bounds.bottom.toFloat(), shadowColor, shadowEndColor, Shader.TileMode.CLAMP)
            else -> LinearGradient((bounds.left + shadowWidth).toFloat(), (bounds.bottom - shadowWidth).toFloat(),
                bounds.left.toFloat(), bounds.bottom.toFloat(), shadowColor, shadowEndColor, Shader.TileMode.CLAMP)
        }
    }

    private fun buildRadiusShadow() {
        shadows.clear()
        val start = shadowRadius.toFloat() / radius
        val stops = floatArrayOf(0f, start - 0.01f, start, 1f)
        val colors = intArrayOf(Color.TRANSPARENT, Color.TRANSPARENT, shadowColor, shadowEndColor)
        for (direction in LEFT..BOTTOM step 2) {
            shadows.add(RectShadow(getRect(direction), getLinearGradient(direction), direction))
        }
        for (direction in LEFT_TOP..LEFT_BOTTOM step 2) {
            shadows.add(getRadialShadow(direction, stops, colors))
        }
    }

    private fun getRadialShadow(direction: Int, stops: FloatArray, colors: IntArray): RadialShadow {
        val centerX: Int
        val centerY: Int
        val startAngel: Int
        val rectF: RectF
        when (direction) {
            LEFT_TOP -> {
                centerX = bounds.left + radius
                centerY = bounds.top + radius
                rectF = RectF(bounds.left.toFloat(), bounds.top.toFloat(),
                    (centerX + radius).toFloat(), (centerY + radius).toFloat())
                startAngel = -180
            }
            RIGHT_TOP -> {
                centerX = bounds.right - radius
                centerY = bounds.top + radius
                rectF = RectF(
                    (centerX - radius).toFloat(), bounds.top.toFloat(),
                    bounds.right.toFloat(), (centerY + radius).toFloat())
                startAngel = -90
            }
            RIGHT_BOTTOM -> {
                centerX = bounds.right - radius
                centerY = bounds.bottom - radius
                rectF = RectF(
                    (centerX - radius).toFloat(), (centerY - radius).toFloat(),
                    bounds.right.toFloat(), bounds.bottom.toFloat())
                startAngel = 0
            }
            else -> {
                centerX = bounds.left + radius
                centerY = bounds.bottom - radius
                rectF = RectF(
                    bounds.left.toFloat(), (centerY - radius).toFloat(),
                    (centerX + radius).toFloat(), bounds.bottom.toFloat())
                startAngel = 90
            }
        }
        return RadialShadow(rectF, centerX, centerY, startAngel, RadialGradient(centerX.toFloat(), centerY.toFloat(),
            (shadowRadius + shadowWidth).toFloat(), colors, stops, Shader.TileMode.CLAMP), direction)
    }

    private fun getRect(direction: Int): Rect {
        return when (direction) {
            LEFT -> Rect(bounds.left, bounds.top + radius, bounds.left + shadowWidth, bounds.bottom - radius)
            LEFT_TOP -> Rect(bounds.left, bounds.top, bounds.left + shadowWidth, bounds.top + shadowWidth)
            TOP -> Rect(bounds.left + radius, bounds.top, bounds.right - radius, bounds.top + shadowWidth)
            RIGHT_TOP -> Rect(bounds.right - shadowWidth, bounds.top, bounds.right, bounds.top + shadowWidth)
            RIGHT -> Rect(bounds.right - shadowWidth, bounds.top + radius, bounds.right, bounds.bottom - radius)
            RIGHT_BOTTOM -> Rect(bounds.right - shadowWidth, bounds.bottom - shadowWidth, bounds.right, bounds.bottom)
            BOTTOM -> Rect(bounds.left + radius, bounds.bottom - shadowWidth, bounds.right - radius, bounds.bottom)
            else -> Rect(bounds.left, bounds.bottom - shadowWidth, bounds.left + shadowWidth, bounds.bottom)
        }
    }

    private fun updateGradient() {
        val start = shadowRadius.toFloat() / radius
        val stops = floatArrayOf(0f, start - 0.01f, start, 1f)
        val colors = intArrayOf(Color.TRANSPARENT, Color.TRANSPARENT, shadowColor, shadowEndColor)
        for (shadow in shadows) {
            shadow.gradient = if (shadow is RadialShadow) {
                RadialGradient(shadow.centerX.toFloat(), shadow.centerY.toFloat(),
                    (shadowRadius + shadowWidth).toFloat(), colors, stops, Shader.TileMode.CLAMP)

            } else getLinearGradient(shadow.direction)
        }
    }

    override fun draw(canvas: Canvas) {
        paint.color = shadowColor
        for (shadow in shadows) {
            paint.shader = shadow.gradient
            when (shadow) {
                is RectShadow -> canvas.drawRect(shadow.rect, paint)
                is RadialShadow -> canvas.drawArc(shadow.rectF, shadow.startAngel.toFloat(), 90f, true, paint)
            }
        }
        paint.color = innerColor
        paint.shader = null
        canvas.drawRoundRect(
            RectF((bounds.left + shadowWidth).toFloat(), (bounds.top + shadowWidth).toFloat(), (bounds.right - shadowWidth).toFloat(),
                (bounds.bottom - shadowWidth).toFloat()), shadowRadius.toFloat(), shadowRadius.toFloat(), paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
        invalidateSelf()
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
        invalidateSelf()
    }

    private open inner class Shadow(val direction: Int, var gradient: Shader)
    private inner class RectShadow(val rect: Rect, gradient: LinearGradient, direction: Int): Shadow(direction, gradient)
    private inner class RadialShadow(val rectF: RectF, val centerX: Int, val centerY: Int,
                                     val startAngel: Int, gradient: RadialGradient, direction: Int): Shadow(direction, gradient)
}