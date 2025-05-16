package com.example.selersmobile

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import android.Manifest
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class AccidenteDatosActivity : AppCompatActivity() {
    private lateinit var spinnerMarca: Spinner
    private lateinit var spinnerModelo: Spinner
    private lateinit var spinnerAnio: Spinner
    private lateinit var spinnerHora: Spinner
    private lateinit var spinnerMinuto: Spinner
    private lateinit var spinnerAmPm: Spinner
    private lateinit var editPatente: EditText
    private lateinit var editDia: EditText
    private lateinit var editMes: EditText
    private lateinit var editAnio: EditText
    private lateinit var editUbicacion: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var btnSiguiente: Button
    private lateinit var btnSubirFoto: Button
    private lateinit var btnTomarFoto: Button
    private lateinit var imagePreview: ImageView

    private var imagenSeleccionadaUri: Uri? = null
    private var imagenTomada: Bitmap? = null

    companion object {
        private const val REQUEST_GALERIA = 1001
        private const val REQUEST_CAMARA = 1002
        private const val PERMISO_CAMARA = 2001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datosaccidente)

        spinnerMarca = findViewById(R.id.spinnerMarca)
        spinnerModelo = findViewById(R.id.spinnerModelo)
        spinnerAnio = findViewById(R.id.spinnerAnio)
        spinnerHora = findViewById(R.id.spinnerHora)
        spinnerMinuto = findViewById(R.id.spinnerMinuto)
        spinnerAmPm = findViewById(R.id.spinnerAmPm)
        editPatente = findViewById(R.id.editPatente)
        editDia = findViewById(R.id.editDia)
        editMes = findViewById(R.id.editMes)
        editAnio = findViewById(R.id.editAnio)
        editUbicacion = findViewById(R.id.editUbicacion)
        editTextDescription = findViewById(R.id.editTextDescription)
        btnSiguiente = findViewById(R.id.btnSiguiente)
        btnSubirFoto = findViewById(R.id.btnSubirFoto)
        btnTomarFoto = findViewById(R.id.btnTomarFoto)
        imagePreview = findViewById(R.id.imagePreview)

        imagePreview.setImageURI(imagenSeleccionadaUri)
        imagePreview.visibility = View.VISIBLE

        cargarDatosEnSpinners()

        btnSubirFoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_GALERIA)
        }

        btnTomarFoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), PERMISO_CAMARA)
            } else {
                abrirCamara()
            }
        }

        btnSiguiente.setOnClickListener {
            validarYEnviar()
        }

        val backArrow = findViewById<ImageView>(R.id.backArrow)
        backArrow.setOnClickListener {
            finish()
        }

        val tipoVehiculo = intent.getStringExtra("vehiculo")
        Toast.makeText(this, "Vehículo seleccionado: $tipoVehiculo", Toast.LENGTH_SHORT).show()

        val title = findViewById<TextView>(R.id.title)
        title.text = "Datos del accidente con $tipoVehiculo"
    }

    private fun abrirCamara() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CAMARA)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISO_CAMARA && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            abrirCamara()
        } else {
            Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_GALERIA -> {
                    imagenSeleccionadaUri = data.data
                    imagePreview.setImageURI(imagenSeleccionadaUri)
                }
                REQUEST_CAMARA -> {
                    imagenTomada = data.extras?.getParcelable("data")
                    imagePreview.setImageBitmap(imagenTomada)
                }
            }
        }
    }

    private fun cargarDatosEnSpinners() {
        val marcas = listOf("Seleccione una marca", "Toyota", "Ford", "Chevrolet", "Renault", "Volkswagen", "Fiat", "Honda")
        val modelos = listOf("Seleccione un modelo", "Corolla", "Focus", "Onix", "Clio", "Gol", "Cronos", "Civic")
        val anios = listOf("Seleccione un año") + (2019..2025).map { it.toString() }
        val horas = listOf("HH") + (1..12).map { it.toString().padStart(2, '0') }
        val minutos = listOf("MM", "00", "15", "30", "45")
        val amPm = listOf("AM/PM", "AM", "PM")

        spinnerMarca.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, marcas)
        spinnerModelo.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, modelos)
        spinnerAnio.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, anios)
        spinnerHora.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, horas)
        spinnerMinuto.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, minutos)
        spinnerAmPm.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, amPm)
    }


    private fun validarYEnviar() {
        val patente = editPatente.text.toString().trim()
        val dia = editDia.text.toString().trim()
        val mes = editMes.text.toString().trim()
        val anioFecha = editAnio.text.toString().trim()
        val ubicacion = editUbicacion.text.toString().trim()
        val descripcion = editTextDescription.text.toString().trim()

        // Validar campos vacíos
        if (patente.isEmpty()) {
            editPatente.error = "Ingrese la patente"
            editPatente.requestFocus()
            return
        }
        if (dia.isEmpty()) {
            editDia.error = "Ingrese el día"
            editDia.requestFocus()
            return
        }
        if (mes.isEmpty()) {
            editMes.error = "Ingrese el mes"
            editMes.requestFocus()
            return
        }
        if (anioFecha.isEmpty()) {
            editAnio.error = "Ingrese el año"
            editAnio.requestFocus()
            return
        }
        if (ubicacion.isEmpty()) {
            editUbicacion.error = "Ingrese la ubicación"
            editUbicacion.requestFocus()
            return
        }
        if (descripcion.isEmpty()) {
            editTextDescription.error = "Ingrese la descripción"
            editTextDescription.requestFocus()
            return
        }

        // Validación de patente argentina (vieja y nueva)
        val regexPatente = Regex("^[A-Z]{3}[0-9]{3}\$|^[A-Z]{2}[0-9]{3}[A-Z]{2}\$")
        if (!regexPatente.matches(patente)) {
            editPatente.error = "Patente inválida (ej: ABC123 o AB123CD)"
            editPatente.requestFocus()
            return
        }

        // Validación de fecha
        val fechaValida = try {
            val d = dia.toInt()
            val m = mes.toInt()
            val a = anioFecha.toInt()
            d in 1..31 && m in 1..12 && a in 1900..2100
        } catch (e: Exception) {
            false
        }

        if (!fechaValida) {
            Toast.makeText(this, "Ingrese una fecha válida (dd/mm/aaaa)", Toast.LENGTH_SHORT).show()
            return
        }

        // Validación de spinners
        if (spinnerMarca.selectedItemPosition == 0 ||
            spinnerModelo.selectedItemPosition == 0 ||
            spinnerAnio.selectedItemPosition == 0) {
            Toast.makeText(this, "Seleccione marca, modelo y año del vehículo", Toast.LENGTH_SHORT).show()
            return
        }

        if (spinnerHora.selectedItemPosition == 0 ||
            spinnerMinuto.selectedItemPosition == 0 ||
            spinnerAmPm.selectedItemPosition == 0) {
            Toast.makeText(this, "Seleccione una hora válida", Toast.LENGTH_SHORT).show()
            return
        }

        // Validación de foto
        if (imagenSeleccionadaUri == null && imagenTomada == null) {
            Toast.makeText(this, "Debe subir o tomar una foto del accidente", Toast.LENGTH_SHORT).show()
            return
        }


        irASiguientePantalla()
    }

    private fun irASiguientePantalla() {
        val intent = Intent(this, ConfirmarAccidenteActivity::class.java).apply {
            putExtra("marca", spinnerMarca.selectedItem.toString())
            putExtra("modelo", spinnerModelo.selectedItem.toString())
            putExtra("anio", spinnerAnio.selectedItem.toString())
            putExtra("patente", editPatente.text.toString())
            putExtra("fecha", "${editDia.text}/${editMes.text}/${editAnio.text}")
            putExtra("hora", "${spinnerHora.selectedItem}:${spinnerMinuto.selectedItem} ${spinnerAmPm.selectedItem}")
            putExtra("ubicacion", editUbicacion.text.toString())
            putExtra("descripcion", editTextDescription.text.toString())
            imagenSeleccionadaUri?.let {
                putExtra("imagenUri", it.toString())
            }
        }
        startActivity(intent)
    }
}
