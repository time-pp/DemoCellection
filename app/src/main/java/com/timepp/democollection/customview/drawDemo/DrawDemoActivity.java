package com.timepp.democollection.customview.drawDemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.timepp.democollection.R;
import com.timepp.democollection.library.ShadowDrawable;

public class DrawDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_demo);
        com.timepp.democollection.library.ShadowDrawable shadowDrawable = new ShadowDrawable(12, 12);
        findViewById(R.id.shadow_drawable_test).setBackground(shadowDrawable);
        shadowDrawable.setColor(Color.parseColor("#ededed"), Color.parseColor("#00E93030"));

        com.timepp.democollection.library.ShadowDrawable shadowDrawable2 = new ShadowDrawable(12, 12);
        findViewById(R.id.shadow_drawable_test2).setBackground(shadowDrawable2);
        shadowDrawable2.setColor(Color.parseColor("#ededed"), Color.parseColor("#00E93030"));
    }


    private static final String TAG = "DrawDemoActivity";
}
