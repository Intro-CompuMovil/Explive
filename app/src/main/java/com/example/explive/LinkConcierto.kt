package com.example.explive

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity

class LinkConcierto : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_link_concierto)

        val myWebView: WebView = findViewById(R.id.webview)
        myWebView.loadUrl("https://www.tuboleta.com/")

    }
}
