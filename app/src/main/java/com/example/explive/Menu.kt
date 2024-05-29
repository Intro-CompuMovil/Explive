package com.example.explive

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        val listaConciertos = findViewById<ListView>(R.id.listView1)
        val conciertosString = intent.getStringExtra("conciertos") ?: "[]" // Usa un array vacío como predeterminado si no hay datos
        val conciertos = JSONArray(conciertosString)
        val botonTodosConciertos = findViewById<Button>(R.id.todosconciertos)
        val BotonPerfil = findViewById<ImageButton>(R.id.botonperfil)

        val nombresConciertos = obtenerNombresConciertos(conciertos)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, nombresConciertos)
        listaConciertos.adapter = adapter

        val nombreUsuario = intent.getStringExtra("nombreUsuario") ?: "Usuario"

        val textViewSaludo = findViewById<TextView>(R.id.eventTitle)
        textViewSaludo.text = "¡Hola, $nombreUsuario! Estos son los conciertos sugeridos para ti:"

        listaConciertos.setOnItemClickListener { parent, view, position, id ->
            val concierto = conciertos.getJSONObject(position)
            val intent = Intent(this, DetallesConcierto::class.java)
            intent.putExtra("concierto", concierto.toString())
            intent.putExtra("id", position)
            startActivity(intent)
        }

        botonTodosConciertos.setOnClickListener {
            val intent = Intent(this, TotalConciertos::class.java)
            intent.putExtra("conciertos", conciertos.toString())
            startActivity(intent)
        }

        BotonPerfil.setOnClickListener {
            val intent = Intent(this, PerfilUsuario::class.java)
            startActivity(intent)
        }
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