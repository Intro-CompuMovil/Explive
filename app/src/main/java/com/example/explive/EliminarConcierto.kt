package com.example.explive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import org.json.JSONArray

class EliminarConcierto : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eliminar_concierto)
        val conciertosString = intent.getStringExtra("conciertos")
        val conciertosJSONarray = JSONArray(conciertosString)
        //val conciertoString = intent.getStringExtra("concierto")
        //val concierto = JSONObject(conciertoString)
        val btnEliminarConcierto = findViewById<Button>(R.id.eliminar)

        val fechaConcierto = findViewById<TextView>(R.id.fechaConcierto)
        val ciudadConcierto = findViewById<TextView>(R.id.ciudadConcierto)
        val lugarConcierto = findViewById<TextView>(R.id.lugarConcierto)
        val horaConcierto = findViewById<TextView>(R.id.horaConcierto)

        val conciertos = convertJsonArrayToList(conciertosJSONarray)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, conciertos)


        val spinner: Spinner = findViewById(R.id.spinner)
        spinner.adapter = adapter

        btnEliminarConcierto.setOnClickListener {
            val conciertoSeleccionado = spinner.selectedItem.toString()
            val concierto =
                conciertosJSONarray.getJSONObject(conciertos.indexOf(conciertoSeleccionado))
            conciertosJSONarray.remove(conciertos.indexOf(conciertoSeleccionado))
            val intent = intent
            intent.putExtra("conciertos", conciertosJSONarray.toString())
            setResult(RESULT_OK, intent)
            Toast.makeText(this, "Evento eliminado", Toast.LENGTH_SHORT).show()
        }

        val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, conciertos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Asignar el adaptador al Spinner
        spinner.adapter = adapter2

        // Establecer un listener para cuando un ítem es seleccionado
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Asumiendo que cada concierto en el JSONArray tiene los campos: fecha, ciudad, centro_de_eventos, hora, etc.
                val selectedItem = parent.getItemAtPosition(position).toString()
                val concierto = conciertosJSONarray.getJSONObject(position)

                // Actualizar los TextViews con los datos del concierto seleccionado
                fechaConcierto.text = concierto.getString("ciudad")
                ciudadConcierto.text = concierto.getString("centro_de_eventos")
                lugarConcierto.text = concierto.getString("fecha")
                horaConcierto.text = "Hora: ${concierto.getString("hora")}"
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Este método se invoca cuando la selección desaparece de este view
            }
        }

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
