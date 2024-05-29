package com.example.explive

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.explive.databinding.ActivityPerfilUsuarioBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.json.JSONObject
import java.io.IOException

class PerfilUsuario : AppCompatActivity() {

    private lateinit var binding: ActivityPerfilUsuarioBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            binding.fperfil.setImageURI(uri)
        }
    }

    private val takePicturePreview = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let {
            binding.fperfil.setImageBitmap(bitmap)
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize ViewBinding
        binding = ActivityPerfilUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference


        val currentUser = auth.currentUser



        if (currentUser != null) {
            loadUserProfile(currentUser.uid)
        }


        binding.btngaleria.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.btncamara.setOnClickListener {
            requestCamera()
        }

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun loadUserProfile(uid: String) {
        val userRef = database.child("users").child(uid)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    binding.textViewNombre.text = user.name
                    binding.textViewCiudad.text = user.city
                    binding.textViewCorreo.text = user.email
                } else {
                    Toast.makeText(this@PerfilUsuario, "User data not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@PerfilUsuario, "Failed to load user data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun requestCamera() {
        when {
            ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                takePicturePreview.launch(null)
            }
            else -> {
                requestPermission.launch(android.Manifest.permission.CAMERA)
            }
        }
    }

    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            takePicturePreview.launch(null)
        } else {
            Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

}
