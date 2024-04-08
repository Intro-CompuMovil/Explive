package com.example.proyectoentrega1


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.proyectoentrega1.databinding.ActivityMomentosBinding
import org.json.JSONObject

class detallesConcierto : AppCompatActivity() {

    private lateinit var binding: ActivityMomentosBinding
    private val REQUEST_CAMERA_PERMISSION = 101
    private val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_concierto)

        val nombreConcierto = findViewById<TextView>(R.id.eventTitle)
        val fechaConcierto = findViewById<TextView>(R.id.fechaConcierto)
        val ciudadConcierto = findViewById<TextView>(R.id.ciudadConcierto)
        val lugarConcierto = findViewById<TextView>(R.id.lugarConcierto)
        val horaConcierto = findViewById<TextView>(R.id.horaConcierto)

        val btnguardarMomentos = findViewById<Button>(R.id.guardarMomentos)
        val btnirSitio = findViewById<Button>(R.id.irStio)


        val conciertoString = intent.getStringExtra("concierto")
        val concierto = JSONObject(conciertoString)

        if (concierto != null) {
            nombreConcierto.text = concierto.getString("artista")
            fechaConcierto.text = concierto.getString("fecha")
            ciudadConcierto.text = concierto.getString("ciudad")
            lugarConcierto.text = concierto.getString("centro_de_eventos")
            horaConcierto.text = "Hora: ${concierto.getString("hora")}"
        }

        btnguardarMomentos.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
            } else {
                dispatchTakePictureIntent()
            }
        }

        btnirSitio.setOnClickListener {
            val intent = Intent(this, Sitio::class.java)
            startActivity(intent)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent()
                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                        Toast.makeText(this, "El permiso de cámara fue negado permanentemente. Por favor, habilita el permiso en ajustes.", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Se requiere permiso de cámara", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    // Sobrescribe este método para manejar el resultado de la captura de imagen
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            // Aquí puedes usar el Bitmap, por ejemplo, mostrarlo en un ImageView
        }
    }
}