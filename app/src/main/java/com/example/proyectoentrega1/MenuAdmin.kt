package com.example.proyectoentrega1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import org.json.JSONArray

class MenuAdmin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_admin)

        val listaConciertos = findViewById<ListView>(R.id.listViewAdmon)
        val btnAgregar = findViewById<Button>(R.id.btnAgregar)
        val btnEliminar = findViewById<Button>(R.id.btnEliminar)
        val conciertosString = intent.getStringExtra("conciertos")
        val conciertos = JSONArray(conciertosString)

        val nombresConciertos = obtenerNombresConciertos(conciertos)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, nombresConciertos)
        listaConciertos.adapter = adapter

        btnAgregar.setOnClickListener {
            val intent = Intent(this, AgregarConcierto::class.java)
            startActivity(intent)
        }

        btnEliminar.setOnClickListener {
            val intent = Intent(this, EliminarConcierto::class.java)
            intent.putExtra("conciertos", conciertosString)
            startActivity(intent)
        }

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