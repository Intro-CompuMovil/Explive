package com.example.proyectoentrega1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONObject
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    companion object {
        var usuarios = JSONArray()
        var conciertos = JSONArray()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextUsuario = findViewById<EditText>(R.id.username)
        val editTextContrasena = findViewById<EditText>(R.id.password)
        val buttonNotRegistered = findViewById<Button>(R.id.not_registered)
        val buttonForgotpassword = findViewById<Button>(R.id.forgot_password)
        val buttonIngresar = findViewById<Button>(R.id.login_button)

        cargarJSONConciertos()
        cargarJSONUsuarios()

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
                Log.i("MainActivity", "Usuario autenticado")
                val intent = Intent(this, Menu::class.java)
                intent.putExtra("conciertos", conciertos.toString())

                val nombreUsuario = obtenerNombreUsuario(usuario)

                intent.putExtra("nombreUsuario", nombreUsuario)
                startActivity(intent)

                startActivity(intent)
            }
            else if(autenticarAdmin(usuario,contrasena)){
                Log.i("MainActivity", "Admin autenticado")
                val intent = Intent(this, MenuAdmin::class.java)
                intent.putExtra("conciertos", conciertos.toString())
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "Error autenticado credenciales", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun autenticarUsuario(usuario: String, contrasena: String): Boolean {
        if(usuario.isEmpty() || contrasena.isEmpty()){
            Log.i("MainActivity", "Usuario o contraseña vacios")
            return false
        }
        for (i in 0 until usuarios.length()) {
            val usuarioJSON = usuarios.getJSONObject(i)
            if (usuarioJSON.getString("Correo electrónico").equals(usuario) && usuarioJSON.getString("Contraseña").equals(contrasena) && usuarioJSON.getString("Rol").equals("user")) {
                Log.i("MainActivity", "Entroooo")
                return true
            }
        }
        return false
    }

    private fun autenticarAdmin(usuario: String,contrasena: String):Boolean{
        if(usuario.isEmpty() || contrasena.isEmpty()){
            Log.i("MainActivity", "Usuario o contraseña vacios")
            return false
        }
        for (i in 0 until usuarios.length()) {
            val usuarioJSON = usuarios.getJSONObject(i)
            if (usuarioJSON.getString("Correo electrónico").equals(usuario) && usuarioJSON.getString("Contraseña").equals(contrasena) && usuarioJSON.getString("Rol").equals("admin")) {
                Log.i("MainActivity", "Entroooo")
                return true
            }
        }
        return false
    }

    private fun obtenerNombreUsuario(email: String): String {
        for (i in 0 until usuarios.length()) {
            val usuario = usuarios.getJSONObject(i)
            if (usuario.getString("Correo electrónico") == email) {
                // Asume que siempre encuentras el usuario y que el campo se llama "Nombre"
                return usuario.getString("Nombre")
            }
        }
        return "Usuario"
    }

    private fun cargarJSONConciertos() {
        val inputStream = assets.open("conciertos_colombia.json")
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        val json = String(buffer, Charset.defaultCharset())
        val jsonObject = JSONObject(json)
        conciertos = jsonObject.getJSONArray("conciertos")
    }
    private fun cargarJSONUsuarios() {
        val inputStream = assets.open("usuarios.json")
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        val json = String(buffer, Charset.defaultCharset())
        val jsonObject = JSONObject(json)
        usuarios = jsonObject.getJSONArray("usuarios")
    }
}