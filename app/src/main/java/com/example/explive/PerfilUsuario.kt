package com.example.explive

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONObject
import java.io.IOException

class PerfilUsuario : AppCompatActivity() {

    private lateinit var imageView: ImageView

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageView.setImageURI(uri)
        }
    }

    private val takePicturePreview = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let {
            imageView.setImageBitmap(bitmap)
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil_usuario)

        imageView = findViewById<ImageView>(R.id.fperfil)
        val btnGaleria = findViewById<Button>(R.id.btngaleria)
        val btnCamara = findViewById<Button>(R.id.btncamara)


        val nombreA = findViewById<TextView>(R.id.textViewNombre)
        val ciudadA = findViewById<TextView>(R.id.textViewCiudad)
        val correoA = findViewById<TextView>(R.id.textViewCorreo)

        val prefs = getSharedPreferences("prefs_usuarios", Context.MODE_PRIVATE)
        val correoElectronico = prefs.getString("Correo electrónico", "Correo no encontrado")

        val jsonString = cargarJsonDesdeAssets(this, "usuarios.json")
        val listaUsuarios = parseUsuariosJson(jsonString)

        val nombreUsuario = intent.getStringExtra("nombreUsuario") ?: "Usuario"

        for (i in 0 until listaUsuarios.size){
            if (listaUsuarios[i].correoElectronico == correoElectronico){
                nombreA.text = listaUsuarios[i].nombre + " " + listaUsuarios[i].apellido
                ciudadA.text = listaUsuarios[i].ciudadResidencia
                correoA.text = listaUsuarios[i].correoElectronico
            }
        }

        btnGaleria.setOnClickListener {
            getContent.launch("image/*")
        }

        btnCamara.setOnClickListener {
            requestCamera()
        }
    }

    private fun requestCamera() {
        when {
            ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the camera
                takePicturePreview.launch(null)
            }
            else -> {
                // Request camera permission
                requestPermission.launch(android.Manifest.permission.CAMERA)
            }
        }
    }

    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            // Permission is granted, you can take a picture
            takePicturePreview.launch(null)
        } else {
            // Permission is denied, show a message to the user
        }
    }



    fun parseUsuariosJson(jsonString: String): List<Usuario> {
        val listaUsuarios = mutableListOf<Usuario>()

        // Crea un JSONObject a partir de tu String JSON
        val jsonObject = JSONObject(jsonString)

        // Obtiene el JSONArray asociado con la clave "usuarios"
        val usuariosArray = jsonObject.getJSONArray("usuarios")

        // Itera a través de cada elemento del JSONArray
        for (i in 0 until usuariosArray.length()) {
            val usuarioJson = usuariosArray.getJSONObject(i)

            // Extrae cada atributo del objeto JSON del usuario
            val nombre = usuarioJson.getString("Nombre")
            val apellido = usuarioJson.getString("Apellido")
            val ciudadResidencia = usuarioJson.getString("Ciudad de residencia")
            val correoElectronico = usuarioJson.getString("Correo electrónico")
            val contraseña = usuarioJson.getString("Contraseña")
            val repetirContraseña = usuarioJson.getString("Repita la contraseña")
            val rol = usuarioJson.getString("Rol")


            // Crea un objeto Usuario y lo añade a la lista
            val usuario = Usuario(nombre, apellido, ciudadResidencia, correoElectronico, contraseña, repetirContraseña, rol)
            listaUsuarios.add(usuario)
        }

        return listaUsuarios
    }

    fun cargarJsonDesdeAssets(context: Context, nombreArchivo: String): String {
        val contenidoJson: String

        try {
            // Abre el archivo de assets y lee su contenido a un String
            val inputStream = context.assets.open(nombreArchivo)
            val tamaño = inputStream.available()
            val buffer = ByteArray(tamaño)
            inputStream.read(buffer)
            inputStream.close()

            // Construye el String a partir del buffer
            contenidoJson = String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return ""
        }

        return contenidoJson
    }


    data class Usuario(
        val nombre: String,
        val apellido: String,
        val ciudadResidencia: String,
        val correoElectronico: String,
        val contraseña: String,
        val repetirContraseña: String,
        val rol: String
    )

}