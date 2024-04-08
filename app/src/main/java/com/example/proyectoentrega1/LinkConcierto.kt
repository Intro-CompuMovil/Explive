package com.example.proyectoentrega1

import android.os.Bundle
import android.webkit.WebView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LinkConcierto : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_link_concierto)

        val myWebView: WebView = findViewById(R.id.webview)
        myWebView.loadUrl("https://www.tuboleta.com/")

    }
}
