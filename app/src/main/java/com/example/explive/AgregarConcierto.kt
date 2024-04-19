package com.example.explive

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AgregarConcierto : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_concierto)

        val btnAgregar = findViewById<Button>(R.id.btnAgregar)

        btnAgregar.setOnClickListener {
            Toast.makeText(this, "Se ha agregado el evento", Toast.LENGTH_SHORT).show()
        }
    }
}