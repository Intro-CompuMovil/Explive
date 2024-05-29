package com.example.explive

import Concierto
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class AgregarConcierto : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_concierto)

        database = Firebase.database.reference

        val editTextArtista = findViewById<EditText>(R.id.editTextText)
        val editTextCiudad = findViewById<EditText>(R.id.editTextText2)
        val editTextCentroDeEventos = findViewById<EditText>(R.id.editTextText3)
        val editTextFecha = findViewById<EditText>(R.id.editTextText4)
        val editTextHora = findViewById<EditText>(R.id.editTextText5)
        val btnAgregar = findViewById<Button>(R.id.btnAgregar)

        btnAgregar.setOnClickListener {
            val artista = editTextArtista.text.toString().trim()
            val ciudad = editTextCiudad.text.toString().trim()
            val centroDeEventos = editTextCentroDeEventos.text.toString().trim()
            val fecha = editTextFecha.text.toString().trim()
            val hora = editTextHora.text.toString().trim()

            if (artista.isEmpty() || ciudad.isEmpty() || centroDeEventos.isEmpty() || fecha.isEmpty() || hora.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                val concierto = Concierto(artista, ciudad, centroDeEventos, fecha, hora)
                agregarConciertoAFirebase(concierto, editTextArtista, editTextCiudad, editTextCentroDeEventos, editTextFecha, editTextHora)
            }
        }
    }

    private fun agregarConciertoAFirebase(concierto: Concierto, editTextArtista: EditText, editTextCiudad: EditText, editTextCentroDeEventos: EditText, editTextFecha: EditText, editTextHora: EditText) {
        val conciertosRef = database.child("conciertos")

        conciertosRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lastId = snapshot.children.maxOfOrNull { it.key?.toIntOrNull() ?: 0 } ?: 0
                val newId = lastId + 1

                conciertosRef.child(newId.toString()).setValue(concierto)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@AgregarConcierto, "Concierto agregado exitosamente", Toast.LENGTH_SHORT).show()
                            // Limpiar los EditText
                            editTextArtista.text.clear()
                            editTextCiudad.text.clear()
                            editTextCentroDeEventos.text.clear()
                            editTextFecha.text.clear()
                            editTextHora.text.clear()
                            // Finalizar la actividad para volver a la pantalla anterior
                            finish()
                        } else {
                            Toast.makeText(this@AgregarConcierto, "Error al agregar concierto: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AgregarConcierto, "Error al acceder a la base de datos: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}