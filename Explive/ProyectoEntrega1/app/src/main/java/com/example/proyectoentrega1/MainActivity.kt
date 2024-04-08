package com.example.proyectoentrega1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    val usuarios = mutableListOf<JSONObject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextUsuario = findViewById<EditText>(R.id.username)
        val editTextContrasena = findViewById<EditText>(R.id.password)
        val buttonNotRegistered = findViewById<Button>(R.id.not_registered)
        val buttonForgotpassword = findViewById<Button>(R.id.forgot_password)
        val buttonIngresar = findViewById<Button>(R.id.login_button)

        buttonNotRegistered.setOnClickListener {
            val intent = Intent(this, Registro::class.java)
            startActivity(intent)
        }

        buttonForgotpassword.setOnClickListener {
            val intent = Intent(this, ContrasniaOlvidada::class.java)
            startActivity(intent)
        }

        buttonIngresar.setOnClickListener {
            val usuario = editTextUsuario.text.toString()
            val contrasena = editTextContrasena.text.toString()
            if (autenticarUsuario(usuario, contrasena)) {
                val intent = Intent(this, Menu::class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "Error autenticado credenciales", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun autenticarUsuario(usuario: String, contrasena: String): Boolean {
        for (u in usuarios) {
            if (u.getString("usuario") == usuario && u.getString("contrasena") == contrasena) {
                return true
            }
        }
        return false
    }
}