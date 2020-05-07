package com.timepp.democollection.customview.drawDemo;

import android.content.Context;
import android.graphics.*;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class MatrixView extends View {
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RadialGradient mGradient = new RadialGradient(0, 0, 1f,
            Color.BLUE, Color.GREEN, Shader.TileMode.CLAMP);
    private Matrix mMatrix = new Matrix();

    public MatrixView(Context context) {
        this(context, null);
    }

    public MatrixView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MatrixView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mMatrix.setTranslate(50f, 50f);
        mMatrix.postScale(24, 24);
        //mMatrix.setScale(24, 24);
        //mMatrix.postTranslate(50f, 50f);
        mGradient.setLocalMatrix(mMatrix);
        mPaint.setShader(mGradient);
        canvas.drawCircle(50f, 50f, 24, mPaint);

        //mMatrix.setScale(32, 32);
        //mMatrix.postTranslate(100f, 100f);
        mMatrix.setTranslate(100, 100);
        mMatrix.postScale(32  , 32);
        mGradient.setLocalMatrix(mMatrix);
        mPaint.setShader(mGradient);
        canvas.drawCircle(100f, 100f, 32, mPaint);
    }
}
