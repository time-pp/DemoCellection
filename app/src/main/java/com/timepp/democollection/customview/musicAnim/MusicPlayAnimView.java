package com.timepp.democollection.customview.musicAnim;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import com.timepp.democollection.R;

public class MusicPlayAnimView extends View {
    private ValueAnimator mValueAnimator;
    // 用来标识动画进度
    private int mProgress = 0;
    // 步长，为 (最大高度 - 最小高度) / 属性动画值的个数
    private float mGapY = 0;
    private int mRectMaxHeight, mRectMinHeight;
    private int mRectNum;
    private int mRectWidth;
    // 初始高度
    private int[] mRectStartHeight;
    // 矩形的横向坐标
    private int[] mRectX;

    private Paint mPaint;
    public MusicPlayAnimView(Context context) {
        this(context, null);
    }

    public MusicPlayAnimView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MusicPlayAnimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(attrs);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        calculateRectX();
    }

    private void calculateRectX() {
        if (mRectNum <= 1) {
            return ;
        }
        int rectSpace = (getMeasuredWidth() - mRectWidth * mRectNum - getPaddingLeft() - getPaddingRight()) / (mRectNum - 1);
        mRectX = new int[mRectNum];
        int gap = mRectWidth + rectSpace;
        for (int i = 0; i < mRectNum; i++) {
            mRectX[i] = gap * i + getPaddingLeft();
        }
    }

    private void initAttr(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MusicPlayAnimView);
        mRectMaxHeight = typedArray.getDimensionPixelSize(R.styleable.MusicPlayAnimView_rectMaxHeight, 0);
        mRectMinHeight = typedArray.getDimensionPixelSize(R.styleable.MusicPlayAnimView_rectMinHeight, 0);
        mRectWidth = typedArray.getDimensionPixelSize(R.styleable.MusicPlayAnimView_rectWidth, 0);
        mRectNum = typedArray.getInt(R.styleable.MusicPlayAnimView_rect_num, 0);
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mRectStartHeight == null || mRectStartHeight.length != mRectNum) {
            return;
        }
        int bottom = getHeight();
        for (int i = 0; i < mRectNum; i++) {
            canvas.drawRect(mRectX[i], bottom - getRectHeight(i), mRectX[i] + mRectWidth, bottom, mPaint);
        }
    }

    private int getRectHeight(int index) {
        int height = (int) (mRectStartHeight[index] + mProgress * mGapY);
        // 超出最大高度，则开始减小高度，此时的height应为最大高度减去超出的高度
        // 即height = mRectMaxHeight - (height - mRectMaxHeight)
        if (height > mRectMaxHeight) {
            height = mRectMaxHeight * 2 - height;
        }
        // 此时的height可能会小于最小高度，则此时的height应为最小的高度加上小于最小高度的部分
        // 即height = mRectMinHeight + (mRectMinHeight - height)
        if (height < mRectMinHeight) {
            height = mRectMinHeight * 2 - height;
        }
        // 此时新得到的height仍可能超出最大高度，则再次减小高度
        if (height > mRectMaxHeight) {
            height = mRectMaxHeight * 2 - height;
        }
        return height;
    }

    public void setAnimParam(int from, int to, long duration) {
        if (from >= to) {
            throw new RuntimeException("from must bigger than to");
        }
        // 因为整个动画是从最小高度 -> 最大高度 -> 最小高度，所以这里是除以一半的区间
        mGapY = ((float)(mRectMaxHeight - mRectMinHeight)) / (to - from) * 2;
        mValueAnimator = ValueAnimator.ofInt(from, to);
        mValueAnimator.setDuration(duration);
        initValueAnimator();
    }

    private void initValueAnimator() {
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mProgress = (int) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
    }
    public void startAnim() {
        mValueAnimator.start();
    }

    public void stopAnim() {
        mValueAnimator.end();
    }

    public void pauseAnim() {
        mValueAnimator.pause();
    }

    public void resumeAnim() {
        mValueAnimator.resume();
    }

    public void setRectStartHeight(int[] rectStartHeight) {
        setRectStartHeight(rectStartHeight, null);
    }

    public void setRectStartHeight(int[] rectStartHeight, boolean[] rectIsUp) {
        if (rectStartHeight == null || rectStartHeight.length != mRectNum || (rectIsUp != null && rectIsUp.length != mRectNum)) {
            throw new RuntimeException("invalid param");
        }
        this.mRectStartHeight = rectStartHeight;
        if (rectIsUp != null) {
            int length = rectIsUp.length;
            for (int i = 0; i < length; i++) {
                if (!rectIsUp[i]) {
                    mRectStartHeight[i] = mRectMaxHeight * 2 - mRectStartHeight[i];
                }
            }
        }
    }

    public void setColor(int color) {
        mPaint.setColor(color);
        invalidate();
    }
}
