package com.timepp.democollection.other.aidl

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.timepp.democollection.R

class AIDLTestActivity: AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aidl_test)
        findViewById<View>(R.id.start_web_socket).setOnClickListener(this)
        findViewById<View>(R.id.stop_web_socket).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id ?: 0) {
            R.id.start_web_socket -> {

            }
            R.id.stop_web_socket -> {

            }
        }
    }
}