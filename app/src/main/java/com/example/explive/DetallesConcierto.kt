package com.example.explive

import Concierto
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetallesConcierto : AppCompatActivity() {

    private lateinit var concierto: Concierto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_concierto)

        concierto = intent.getParcelableExtra("concierto") ?: Concierto()

        val nombreConcierto = findViewById<TextView>(R.id.eventTitle)
        val fechaConcierto = findViewById<TextView>(R.id.fechaConcierto)
        val ciudadConcierto = findViewById<TextView>(R.id.ciudadConcierto)
        val lugarConcierto = findViewById<TextView>(R.id.lugarConcierto)
        val horaConcierto = findViewById<TextView>(R.id.horaConcierto)

        nombreConcierto.text = concierto.artista
        fechaConcierto.text = concierto.fecha
        ciudadConcierto.text = concierto.ciudad
        lugarConcierto.text = concierto.centro_de_eventos
        horaConcierto.text = "Hora: ${concierto.hora}"
    }
}
