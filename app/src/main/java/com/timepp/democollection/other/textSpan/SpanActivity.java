package com.timepp.democollection.other.textSpan;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.timepp.democollection.R;

public class SpanActivity extends AppCompatActivity {
    private static final String TAG = "SpanActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spannable_string);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView textView = findViewById(R.id.test);
        SpannableString spannableString = new SpannableString("5:29");
        int index = "5:29".indexOf(':');
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.RED);
        spannableString.setSpan(foregroundColorSpan, index + 1, "5:29".length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        foregroundColorSpan = new ForegroundColorSpan(Color.GREEN);
        spannableString.setSpan(foregroundColorSpan, 0, index, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);

        TextView clickTv = findViewById(R.id.span_test);
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder("span click test");
        stringBuilder.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Log.d(TAG, "on span Click: ");
                Toast.makeText(widget.getContext(), "onSpanClick", Toast.LENGTH_SHORT).show();
            }
        }, 0, 5, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        clickTv.setText(stringBuilder);
        clickTv.setMovementMethod(LinkMovementMethod.getInstance());
        clickTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                Toast.makeText(v.getContext(), "on Click", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
