package com.timepp.democollection

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.timepp.democollection.customview.drawDemo.DrawDemoActivity
import com.timepp.democollection.customview.drawDemo.MatrixActivity
import com.timepp.democollection.customview.musicAnim.MusicAnimActivity
import com.timepp.democollection.customview.recyclerviewtest.RecyclerViewActivity
import com.timepp.democollection.customview.scaleview.ScaleActivity
import com.timepp.democollection.other.fingerprint.FingerPrintActivity
import com.timepp.democollection.other.aidl.AIDLTestActivity
import com.timepp.democollection.other.recyclerview.AnotherRecyclerViewActivity
import com.timepp.democollection.other.scrollTest.ScrollActivity
import com.timepp.democollection.other.webview.WebViewActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        startActivity(Intent(this, when (v?.id ?: 0) {
            R.id.music_play_anim -> MusicAnimActivity::class.java
            R.id.shadow_drawable_test -> DrawDemoActivity::class.java
            R.id.recycler_view_test -> RecyclerViewActivity::class.java
            R.id.finger_test -> FingerPrintActivity::class.java
            R.id.scroll_test -> ScrollActivity::class.java
            R.id.matrix_test -> MatrixActivity::class.java
            R.id.another_recycler_view_test -> AnotherRecyclerViewActivity::class.java
            R.id.web_view_test -> WebViewActivity::class.java
            R.id.scale_test -> ScaleActivity::class.java
            R.id.aidl_test -> AIDLTestActivity::class.java
            else -> MainActivity::class.java
        }))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.music_play_anim).setOnClickListener(this)
        findViewById<View>(R.id.shadow_drawable_test).setOnClickListener(this)
        findViewById<View>(R.id.recycler_view_test).setOnClickListener(this)
        findViewById<View>(R.id.finger_test).setOnClickListener(this)
        findViewById<View>(R.id.scroll_test).setOnClickListener(this)
        findViewById<View>(R.id.matrix_test).setOnClickListener(this)
        findViewById<View>(R.id.another_recycler_view_test).setOnClickListener(this)
        findViewById<View>(R.id.web_view_test).setOnClickListener(this)
        findViewById<View>(R.id.scale_test).setOnClickListener(this)
        findViewById<View>(R.id.aidl_test).setOnClickListener(this)
    }
}
