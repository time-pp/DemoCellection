package com.timepp.democollection.customview.musicAnim;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.timepp.democollection.R;
import com.timepp.democollection.customview.GuidePoint;

public class MusicAnimActivity  extends AppCompatActivity implements View.OnClickListener {

    private boolean mIsPlay;
    private MusicPlayAnimView mMusicPlayAnimView;
    private GuidePoint mGuidePoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_anim);
        findViewById(R.id.start_anim).setOnClickListener(this);
        findViewById(R.id.pause_anim).setOnClickListener(this);
        findViewById(R.id.stop_anim).setOnClickListener(this);
        mMusicPlayAnimView = findViewById(R.id.music_play_anim);
        mMusicPlayAnimView.setAnimParam(0, 3600, 1000);
        mMusicPlayAnimView.setColor(Color.parseColor("#e93030"));
        mMusicPlayAnimView.setRectStartHeight(new int[]{12, 36, 18, 30}, new boolean[]{true, false, true, false});
        mGuidePoint = findViewById(R.id.guide_point);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_anim:
                mMusicPlayAnimView.startAnim();
                mGuidePoint.startAnim();
                mIsPlay = true;
                break;
            case R.id.pause_anim:
                if (mIsPlay) {
                    mMusicPlayAnimView.pauseAnim();
                    mIsPlay = false;
                } else {
                    mMusicPlayAnimView.resumeAnim();
                    mIsPlay = true;
                }
                break;
            case R.id.stop_anim:
                mMusicPlayAnimView.stopAnim();
                mGuidePoint.stopAnim();
                mIsPlay = false;
                break;
        }
    }
}
