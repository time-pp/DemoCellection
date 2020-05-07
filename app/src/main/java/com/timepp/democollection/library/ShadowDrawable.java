package com.timepp.democollection.library;

import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

public class ShadowDrawable extends Drawable {
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final int mShadowWidth, mShadowRadius, mRadius;
    private int mInnerColor;
    private int[] mShadowColors;
    private float[] mShadowPositions = {0f, 0.2f, 0.4f, 1f};
    private RectShadow mLeftShadow, mTopShadow;
    private RadialShadow mLeftTopShadow, mRightTopShadow;
    private RectF mInnerRect;
    private boolean mHadDraw;

    public ShadowDrawable(int shadowWidth, int shadowRadius) {
        if (shadowWidth <= 0 || shadowRadius <= 0) {
            throw new IllegalArgumentException("shadow width and shadow radius must > 0");
        }
        mShadowWidth = shadowWidth;
        mShadowRadius = shadowRadius;
        mRadius = mShadowWidth + mShadowRadius;
    }

    public void setColor(int shadowColor, int innerColor) {
        mShadowColors = new int[]{shadowColor, shadowColor & 0x7FFFFFFF, shadowColor & 0x33FFFFFF, shadowColor & 0x00FFFFFF};
        mInnerColor = innerColor;
        updateGradient();
        invalidateSelf();
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        if (bounds == null || bounds.isEmpty()) {
            return;
        }
        buildShadows();
        mInnerRect = new RectF(bounds);
        mInnerRect.inset(mShadowWidth, mShadowWidth);
    }

    private void buildShadows() {
        Rect bounds = getBounds();
        mLeftShadow = new RectShadow();
        mLeftShadow.rect = new Rect(bounds.left, bounds.top + mRadius,
                bounds.left + mShadowWidth, bounds.bottom - mRadius);
        mLeftShadow.gradient = new LinearGradient(bounds.left + mShadowWidth, bounds.top, bounds.left,
                bounds.top, mShadowColors, mShadowPositions, Shader.TileMode.CLAMP);
        mTopShadow = new RectShadow();
        mTopShadow.rect = new Rect(bounds.left + mRadius, bounds.top, bounds.right - mRadius,
                bounds.top + mShadowWidth);
        mTopShadow.gradient = new LinearGradient(bounds.left, bounds.top + mShadowWidth, bounds.left,
                bounds.top, mShadowColors, mShadowPositions, Shader.TileMode.CLAMP);

        float start = ((float)mShadowRadius) / mRadius;
        float[] stops = {0f, start - 0.002f, start - 0.001f, start + (1 - start) * mShadowPositions[1], start + (1 - start) * mShadowPositions[2], 1f};
        int[] colors = {Color.TRANSPARENT, Color.TRANSPARENT, mShadowColors[0], mShadowColors[1], mShadowColors[2], mShadowColors[3]};

        mLeftTopShadow = new RadialShadow();
        mLeftTopShadow.startAngel = -180;
        mLeftTopShadow.centerX = bounds.left + mRadius;
        mLeftTopShadow.centerY = bounds.top + mRadius;
        mLeftTopShadow.rectF = new RectF(bounds.left, bounds.top,
                mLeftTopShadow.centerX + mRadius, mLeftTopShadow.centerY + mRadius);
        mLeftTopShadow.gradient = new RadialGradient(mLeftTopShadow.centerX, mLeftTopShadow.centerY,
                mRadius, colors, stops, Shader.TileMode.CLAMP);

        mRightTopShadow = new RadialShadow();
        mRightTopShadow.startAngel = -90;
        mRightTopShadow.centerX = bounds.right - mRadius;
        mRightTopShadow.centerY = bounds.top + mRadius;
        mRightTopShadow.rectF = new RectF(mRightTopShadow.centerX - mRadius, bounds.top,
                bounds.right, mRightTopShadow.centerY + mRadius);
        mRightTopShadow.gradient = new RadialGradient(mRightTopShadow.centerX, mRightTopShadow.centerY,
                mRadius, colors, stops, Shader.TileMode.CLAMP);
    }

    private void updateGradient() {
        if (mLeftShadow == null || mTopShadow == null || mLeftTopShadow == null || mRightTopShadow == null) {
            return;
        }
        Rect bounds = getBounds();
        mLeftShadow.gradient = new LinearGradient(bounds.left + mShadowWidth, bounds.top, bounds.left,
                bounds.top, mShadowColors, mShadowPositions, Shader.TileMode.CLAMP);
        mTopShadow.gradient = new LinearGradient(bounds.left, bounds.top + mShadowWidth, bounds.left,
                bounds.top, mShadowColors, mShadowPositions, Shader.TileMode.CLAMP);

        float start = ((float)mShadowRadius) / mRadius;
        float[] stops = {0f, start - 0.002f, start - 0.001f, start + (1 - start) * mShadowPositions[1], start + (1 - start) * mShadowPositions[2], 1f};
        int[] colors = {Color.TRANSPARENT, Color.TRANSPARENT, mShadowColors[0], mShadowColors[1], mShadowColors[2], mShadowColors[3]};
        mLeftTopShadow.gradient = new RadialGradient(mLeftTopShadow.centerX, mLeftTopShadow.centerY,
                mRadius, colors, stops, Shader.TileMode.CLAMP);
        mRightTopShadow.gradient = new RadialGradient(mRightTopShadow.centerX, mRightTopShadow.centerY,
                mRadius, colors, stops, Shader.TileMode.CLAMP);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        mPaint.setColor(mShadowColors[0]);
        canvas.save();
        drawShadow(canvas);
        canvas.rotate(180, getBounds().centerX(), getBounds().centerY());
        drawShadow(canvas);
        canvas.restore();
        mPaint.setShader(null);
        mPaint.setColor(mInnerColor);
        canvas.drawRoundRect(mInnerRect, mShadowRadius, mShadowRadius, mPaint);
    }

    private static final String TAG = "ShadowDrawable";

    private void drawShadow(Canvas canvas) {
        mPaint.setShader(mLeftShadow.gradient);
        canvas.drawRect(mLeftShadow.rect, mPaint);
        mPaint.setShader(mLeftTopShadow.gradient);
        canvas.drawArc(mLeftTopShadow.rectF, mLeftTopShadow.startAngel, 90, true,mPaint);
        mPaint.setShader(mTopShadow.gradient);
        canvas.drawRect(mTopShadow.rect, mPaint);
        mPaint.setShader(mRightTopShadow.gradient);
        canvas.drawArc(mRightTopShadow.rectF, mRightTopShadow.startAngel, 90, true,mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    private class RectShadow {
        private Rect rect;
        private LinearGradient gradient;
    }

    private class RadialShadow {
        private RectF rectF;
        private RadialGradient gradient;
        private int centerX, centerY;
        private int startAngel;
    }
}
