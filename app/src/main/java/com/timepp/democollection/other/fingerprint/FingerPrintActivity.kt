package com.timepp.democollection.other.fingerprint

import android.annotation.TargetApi
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.timepp.democollection.R

class FingerPrintActivity : AppCompatActivity(), View.OnClickListener {
    private val callback = FingerCallback()
    private lateinit var stateTv: TextView
    override fun onClick(v: View?) {
        v?.let {
            when (v.id) {
                R.id.start_finger_print -> {
                    Toast.makeText(this@FingerPrintActivity, "请验证指纹", Toast.LENGTH_SHORT).show()
                    stateTv.text = "等待验证指纹"
                    FingerPrintUtils.getInstance(this).startListener(this, callback)
                }
                R.id.stop_finger_print -> {
                    FingerPrintUtils.getInstance(this).stopListener()
                    stateTv.text = "指纹认证已取消"
                    Toast.makeText(this@FingerPrintActivity, "验证取消", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.start_finger_print).setOnClickListener(this)
        findViewById<View>(R.id.stop_finger_print).setOnClickListener(this)
        stateTv = findViewById(R.id.finger_state)
    }

    @TargetApi(Build.VERSION_CODES.M)
    private inner class FingerCallback : FingerprintManager.AuthenticationCallback() {
        override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult?) {
            super.onAuthenticationSucceeded(result)
            stateTv.text = "指纹认证通过"
            Toast.makeText(this@FingerPrintActivity, "验证成功", Toast.LENGTH_SHORT).show()
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            stateTv.text = "指纹认证未通过"
            Toast.makeText(this@FingerPrintActivity, "验证失败", Toast.LENGTH_SHORT).show()
        }

        override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
            super.onAuthenticationHelp(helpCode, helpString)
            stateTv.text = "helpCode = $helpCode, helpString = $helpString"
            Toast.makeText(this@FingerPrintActivity, helpString, Toast.LENGTH_SHORT).show()
        }

        override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
            super.onAuthenticationError(errorCode, errString)
            stateTv.text = "errorCode = $errorCode, errorString = $errString"
            Toast.makeText(this@FingerPrintActivity, errString, Toast.LENGTH_SHORT).show()
        }
    }
}