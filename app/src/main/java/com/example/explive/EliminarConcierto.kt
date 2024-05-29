package com.example.explive

import Concierto
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class EliminarConcierto : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var spinner: Spinner
    private lateinit var fechaConcierto: TextView
    private lateinit var ciudadConcierto: TextView
    private lateinit var lugarConcierto: TextView
    private lateinit var horaConcierto: TextView
    private lateinit var btnEliminarConcierto: Button

    private val conciertosList = mutableListOf<Concierto>()
    private val conciertosNames = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eliminar_concierto)

        database = Firebase.database.reference.child("conciertos")

        spinner = findViewById(R.id.spinner)
        fechaConcierto = findViewById(R.id.fechaConcierto)
        ciudadConcierto = findViewById(R.id.ciudadConcierto)
        lugarConcierto = findViewById(R.id.lugarConcierto)
        horaConcierto = findViewById(R.id.horaConcierto)
        btnEliminarConcierto = findViewById(R.id.eliminar)

        cargarConciertos()

        btnEliminarConcierto.setOnClickListener {
            val position = spinner.selectedItemPosition
            if (position != AdapterView.INVALID_POSITION) {
                val conciertoSeleccionado = conciertosList[position]
                eliminarConcierto(conciertoSeleccionado)
            } else {
                Toast.makeText(this, "Por favor, seleccione un concierto", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cargarConciertos() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                conciertosList.clear()
                conciertosNames.clear()
                for (conciertoSnapshot in snapshot.children) {
                    val concierto = conciertoSnapshot.getValue(Concierto::class.java)
                    if (concierto != null) {
                        conciertosList.add(concierto)
                        conciertosNames.add("${concierto.artista} - ${concierto.ciudad}")
                    }
                }
                val adapter = ArrayAdapter(this@EliminarConcierto, android.R.layout.simple_spinner_item, conciertosNames)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EliminarConcierto, "Error al cargar los conciertos", Toast.LENGTH_SHORT).show()
            }
        })

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val concierto = conciertosList[position]
                fechaConcierto.text = concierto.fecha
                ciudadConcierto.text = concierto.ciudad
                lugarConcierto.text = concierto.centro_de_eventos
                horaConcierto.text = "Hora: ${concierto.hora}"
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No se seleccionó nada
            }
        }
    }

    private fun eliminarConcierto(concierto: Concierto) {
        val conciertoRef = database.orderByChild("artista").equalTo(concierto.artista)
        conciertoRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (conciertoSnapshot in snapshot.children) {
                    conciertoSnapshot.ref.removeValue().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@EliminarConcierto, "Evento eliminado", Toast.LENGTH_SHORT).show()
                            cargarConciertos()  // Recargar la lista de conciertos
                        } else {
                            Toast.makeText(this@EliminarConcierto, "Error al eliminar el evento: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EliminarConcierto, "Error al acceder a la base de datos: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
