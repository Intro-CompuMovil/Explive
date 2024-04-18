package com.example.proyectoentrega1

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONArray

class TotalConciertos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_total_conciertos)
        val listaConciertos = findViewById<ListView>(R.id.listView1)
        val conciertosString = intent.getStringExtra("conciertos")
        val conciertos = JSONArray(conciertosString)

        val nombresConciertos = obtenerNombresConciertos(conciertos)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, nombresConciertos)
        listaConciertos.adapter = adapter

        val nombreUsuario = intent.getStringExtra("nombreUsuario") ?: "Usuario"

        val textViewSaludo = findViewById<TextView>(R.id.eventTitle)
        textViewSaludo.text = "Estos son los conciertos:"

        val intent = Intent(this, detallesConcierto::class.java)

        listaConciertos.setOnItemClickListener { parent, view, position, id ->
            val concierto = conciertos.getJSONObject(position)
            intent.putExtra("concierto", concierto.toString())
            startActivity(intent)
        }
    }

    private fun obtenerNombresConciertos(destinos: JSONArray): MutableList<String> {
        val nombresConciertos = mutableListOf<String>()
        for (i in 0 until destinos.length()) {
            val conciertos = destinos.getJSONObject(i)
            nombresConciertos.add(conciertos.getString("artista") + " " + conciertos.getString("ciudad"))
        }
        return nombresConciertos
    }
}