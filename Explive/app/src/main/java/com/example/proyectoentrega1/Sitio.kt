package com.example.proyectoentrega1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Sitio : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sitio)

        val btnirSitio = findViewById<Button>(R.id.sitiocomprabtn)

        btnirSitio.setOnClickListener {
            val intent = Intent(this, LinkConcierto::class.java)
            startActivity(intent)
        }
    }
}