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
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import com.bumptech.glide.Glide
import com.example.explive.databinding.ActivityMenuBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class Menu : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val conciertosString = intent.getStringExtra("conciertos") ?: "[]" // Usa un array vacío como predeterminado si no hay datos
        val conciertos = JSONArray(conciertosString)

        binding = ActivityMenuBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)

        val nombresConciertos = obtenerNombresConciertos(conciertos)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, nombresConciertos)
        binding.listView1.adapter = adapter

        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

        val currentUser = auth.currentUser

        if (currentUser != null) {
            loadUserProfile(currentUser.uid)
        }

        binding.listView1.setOnItemClickListener { parent, view, position, id ->
            val concierto = conciertos.getJSONObject(position)
            val intent = Intent(this, DetallesConcierto::class.java)
            intent.putExtra("concierto", concierto.toString())
            intent.putExtra("id", position)
            startActivity(intent)
        }

        binding.todosconciertos.setOnClickListener {
            val intent = Intent(this, TotalConciertos::class.java)
            intent.putExtra("conciertos", conciertos.toString())
            startActivity(intent)
        }

        binding.botonperfil.setOnClickListener {
            val intent = Intent(this, PerfilUsuario::class.java)
            startActivity(intent)
        }
    }

    private fun loadUserProfile(uid: String) {
        val userRef = database.child("users").child(uid)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    binding.eventTitle.text = "¡Hola ${getFirstWord(user.name)}!, estos son los conciertos sugeridos para ti:"
                } else {
                    Toast.makeText(this@Menu, "User data not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Menu, "Failed to load user data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun obtenerNombresConciertos(destinos: JSONArray): MutableList<String> {
        val nombresConciertos = mutableListOf<String>()
        for (i in 0 until destinos.length()) {
            val conciertos = destinos.getJSONObject(i)
            nombresConciertos.add(conciertos.getString("artista") + " " + conciertos.getString("ciudad"))
        }
        return nombresConciertos
    }

    fun getFirstWord(text: String): String? {
        val words = text.split(" ")
        return if (words.isNotEmpty()) words[0] else null
    }

}


