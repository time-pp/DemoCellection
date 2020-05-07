package com.timepp.democollection.customview.drawDemo;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CustomView extends View {
    private Paint mPaint = new Paint();
    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private static final String TAG = "CustomView";
    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
        canvas.drawCircle(width / 2, height / 2, height / 4, mPaint);
        mPaint.setColor(Color.RED);
        mPaint.setTextSize(100);
        mPaint.setStyle(Paint.Style.FILL);
        float textWidth = mPaint.measureText("1");
        float ascent = mPaint.ascent();
        float descent = mPaint.descent();
        float textHeight = descent - ascent;

        Log.d(TAG, "onDraw: " + ascent + ',' + descent + ',' + textHeight);

        canvas.drawText("1", width / 2 - textWidth / 2, height / 2 - (ascent + textHeight / 2), mPaint);
        mPaint.setColor(Color.BLUE);
        canvas.drawLine(0, height / 2, width, height / 2, mPaint);
        //base
        mPaint.setColor(Color.GREEN);
        canvas.drawLine(0, height / 2 - (ascent + textHeight / 2), width, height / 2 - (ascent + textHeight / 2), mPaint);
        // ascent
        mPaint.setColor(Color.YELLOW);
        canvas.drawLine(0, height / 2 - (ascent + textHeight / 2) + ascent, width, height / 2 - (ascent + textHeight / 2) + ascent, mPaint);
        //descent
        mPaint.setColor(Color.RED);
        canvas.drawLine(0, height / 2 - (ascent + textHeight / 2) + descent, width, height / 2 - (ascent + textHeight / 2) + descent, mPaint);
        canvas.translate(50, 50);
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(100);
        textPaint.setColor(Color.BLACK);
        StaticLayout staticLayout = new StaticLayout("Hello World", 0, "Hello World".length(), textPaint, 300, Layout.Alignment.ALIGN_NORMAL, 1, 0, false, TextUtils.TruncateAt.END, 0);
        staticLayout.draw(canvas);



    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure: ");
    }
}
