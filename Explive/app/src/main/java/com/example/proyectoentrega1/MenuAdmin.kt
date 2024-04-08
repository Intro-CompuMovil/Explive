package com.example.proyectoentrega1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MenuAdmin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_admin)

        val btnAgregar = findViewById<Button>(R.id.btnAgregar)
        val btnEliminar = findViewById<Button>(R.id.btnEliminar)
        val conciertosString = intent.getStringExtra("conciertos")



        btnAgregar.setOnClickListener {
            val intent = Intent(this, AgregarConcierto::class.java)
            startActivity(intent)
        }

        btnEliminar.setOnClickListener {
            val intent = Intent(this, EliminarConcierto::class.java)
            intent.putExtra("conciertos", conciertosString)
            startActivity(intent)
        }
    }
}