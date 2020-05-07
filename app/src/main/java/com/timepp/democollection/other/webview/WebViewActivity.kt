package com.timepp.democollection.other.webview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebView
import com.timepp.democollection.R

class WebViewActivity : AppCompatActivity() {
    private val webView by lazy { findViewById<WebView>(R.id.web_view) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        val webSettings = webView.settings
        webSettings.javaScriptCanOpenWindowsAutomatically = true//设置js可以直接打开窗口，如window.open()，默认为false
        webSettings.javaScriptEnabled = true//是否允许JavaScript脚本运行，默认为false。设置true时，会提醒可能造成XSS漏洞
        webSettings.setSupportZoom(true)//是否可以缩放，默认true
        webSettings.builtInZoomControls = true//是否显示缩放按钮，默认false
        webSettings.useWideViewPort = true//设置此属性，可任意比例缩放。大视图模式
        webSettings.loadWithOverviewMode = true//和setUseWideViewPort(true)一起解决网页自适应问题
        webSettings.setAppCacheEnabled(true)//是否使用缓存
        webSettings.domStorageEnabled = true//开启本地DOM存储
        webSettings.loadsImagesAutomatically = true // 加载图片
        webSettings.mediaPlaybackRequiresUserGesture = false//播放音频，多媒体需要用户手动？设置为false为可自动播放
        webView.loadUrl("https://m.10jqka.com.cn/stockpage/hs_300033")
    }
}