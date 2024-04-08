package com.example.proyectoentrega1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import org.json.JSONArray

class EliminarConcierto : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eliminar_concierto)
        val conciertosString = intent.getStringExtra("conciertos")
        val conciertosJSONarray = JSONArray(conciertosString)
        val btnEliminarConcierto = findViewById<Button>(R.id.eliminar)


        val conciertos = convertJsonArrayToList(conciertosJSONarray)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, conciertos)


        val spinner: Spinner = findViewById(R.id.spinner)
        spinner.adapter = adapter

        btnEliminarConcierto.setOnClickListener {
            val conciertoSeleccionado = spinner.selectedItem.toString()
            val concierto = conciertosJSONarray.getJSONObject(conciertos.indexOf(conciertoSeleccionado))
            conciertosJSONarray.remove(conciertos.indexOf(conciertoSeleccionado))
            val intent = intent
            intent.putExtra("conciertos", conciertosJSONarray.toString())
            setResult(RESULT_OK, intent)
            Toast.makeText(this, "Evento eliminado", Toast.LENGTH_SHORT).show()
        }


    }
    fun convertJsonArrayToList(jsonArray: JSONArray): ArrayList<String> {
        val listData = ArrayList<String>()
        for (i in 0 until jsonArray.length()) {
            val item = jsonArray.getJSONObject(i).getString("artista")
            listData.add(item)
        }
        return listData
    }

}