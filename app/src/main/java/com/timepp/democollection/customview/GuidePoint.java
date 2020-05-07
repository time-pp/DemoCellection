package com.timepp.democollection.customview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class GuidePoint extends View {
    public GuidePoint(Context context) {
        this(context, null);
    }

    public GuidePoint(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuidePoint(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint.setStyle(Paint.Style.FILL);
        mAnim = ValueAnimator.ofFloat(mSmallSize, mBigSize);
        mAnim.setRepeatMode(ValueAnimator.REVERSE);
        mAnim.setRepeatCount(ValueAnimator.INFINITE);
        mAnim.setDuration(DURATION);
        mAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentSize = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
    }

    private static final int ALPHA = 0x7FFFFFFF;
    private static final int DURATION = 1000;
    private int mSmallSize = 9;
    private int mBigSize = 18;
    private float mCurrentSize = mSmallSize;
    private int mColor = 0xFF4691EE;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private ValueAnimator mAnim;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        mPaint.setColor(mColor & ALPHA);
        canvas.drawCircle(centerX, centerY, mCurrentSize, mPaint);
        mPaint.setColor(mColor);
        canvas.drawCircle(centerX, centerY, mSmallSize, mPaint);
    }

    public void startAnim() {
        mAnim.start();
    }

    public void stopAnim() {
        mAnim.end();
    }

}
