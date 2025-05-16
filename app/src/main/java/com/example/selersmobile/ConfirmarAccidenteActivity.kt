package com.example.selersmobile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ConfirmarAccidenteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmaraccidente)

        val btnCancelar = findViewById<Button>(R.id.btnCancelar)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)


        val marca = intent.getStringExtra("marca") ?: ""
        val modelo = intent.getStringExtra("modelo") ?: ""
        val anio = intent.getStringExtra("anio") ?: ""
        val patente = intent.getStringExtra("patente") ?: ""
        val fecha = intent.getStringExtra("fecha") ?: ""
        val hora = intent.getStringExtra("hora") ?: ""
        val ubicacion = intent.getStringExtra("ubicacion") ?: ""
        val descripcion = intent.getStringExtra("descripcion") ?: ""
        val tipoVehiculo = intent.getStringExtra("vehiculo") ?: ""

        cargarDatoEnCard(R.id.cardTipoVehiculo, "Tipo de Vehículo", tipoVehiculo)
        cargarDatoEnCard(R.id.cardDatosVehiculo, "Datos del Vehículo", "$marca $modelo $anio")
        cargarDatoEnCard(R.id.cardPatente, "Patente", patente)
        cargarDatoEnCard(R.id.cardFecha, "Fecha", fecha)
        cargarDatoEnCard(R.id.cardHora, "Hora", hora)
        cargarDatoEnCard(R.id.cardUbicacion, "Ubicación", ubicacion)
        cargarDatoEnCard(R.id.cardDescripcion, "Descripción", descripcion)

        btnGuardar.setOnClickListener {
            // Generar ID aleatorio
            val idGestion = (100000..999999).random()

            AlertDialog.Builder(this)
                .setTitle("Gestión exitosa")
                .setMessage("La gestión fue realizada con éxito.\nID de gestión: $idGestion")
                .setPositiveButton("OK") { _, _ ->
                    // Ir a MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish() // Cierra esta pantalla
                }
                .show()
        }


        btnCancelar.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Cancelar gestión")
                .setMessage("¿Estás seguro de que querés cancelar esta gestión?")
                .setPositiveButton("Sí") { _, _ ->
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    private fun cargarDatoEnCard(cardId: Int, label: String, valor: String) {
        val cardView = findViewById<View>(cardId)
        val textLabel = cardView.findViewById<TextView>(R.id.fieldLabel)
        val textValue = cardView.findViewById<TextView>(R.id.fieldValue)
        val editIcon = cardView.findViewById<ImageView>(R.id.editIcon)

        textLabel.text = label
        textValue.text = valor

        editIcon.setOnClickListener {
            mostrarDialogoEdicion(textLabel.text.toString(), textValue)
        }
    }

    private fun mostrarDialogoEdicion(titulo: String, textViewParaActualizar: TextView) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_field, null)
        val input = dialogView.findViewById<EditText>(R.id.editFieldInput)
        input.setText(textViewParaActualizar.text.toString())

        val dialog = AlertDialog.Builder(this)
            .setTitle("Editar $titulo")
            .setView(dialogView)
            .setPositiveButton("Guardar", null) // manejamos manualmente luego
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.setOnShowListener {
            val botonGuardar = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            botonGuardar.setOnClickListener {
                val nuevoTexto = input.text.toString().trim()

                if (titulo == "Tipo de Vehículo") {
                    val valoresValidos = listOf("bicicleta", "monopatin", "motocicleta")
                    if (!valoresValidos.contains(nuevoTexto.lowercase())) {
                        input.error = "Solo se permite: bicicleta, monopatin o motocicleta"
                        return@setOnClickListener
                    }
                }
                // Validación personalizada
                val esValido = when (titulo) {
                    "Marca", "Modelo", "Ubicación", "Descripción" -> {
                        if (nuevoTexto.isEmpty()) {
                            input.error = "$titulo no puede estar vacío"
                            false
                        } else true
                    }
                    "Año" -> {
                        if (!nuevoTexto.matches(Regex("\\d{4}"))) {
                            input.error = "Ingrese un año válido de 4 dígitos"
                            false
                        } else {
                            val anio = nuevoTexto.toInt()
                            if (anio < 1900 || anio > 2100) {
                                input.error = "El año debe estar entre 1900 y 2100"
                                false
                            } else true
                        }
                    }
                    "Patente" -> {
                        val patenteRegex = Regex("^(?:[A-Z]{2}\\d{3}[A-Z]{2}|[A-Z]{3}\\d{3})$")
                        if (!nuevoTexto.matches(patenteRegex)) {
                            input.error = "Formato de patente inválido"
                            false
                        } else true
                    }
                    "Fecha" -> {
                        val fechaRegex = Regex("^\\d{2}/\\d{2}/\\d{4}\$")
                        if (!nuevoTexto.matches(fechaRegex)) {
                            input.error = "Formato de fecha inválido (ej: 12/09/2024)"
                            false
                        } else true
                    }
                    "Hora" -> {
                        val horaRegex = Regex("^([01]\\d|2[0-3]):[0-5]\\d\$")
                        if (!nuevoTexto.matches(horaRegex)) {
                            input.error = "Formato de hora inválido (ej: 14:30)"
                            false
                        } else true
                    }
                    else -> true
                }

                if (esValido) {
                    textViewParaActualizar.text = nuevoTexto
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }

}
