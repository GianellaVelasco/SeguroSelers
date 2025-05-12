package com.example.selersmobile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class BiciActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bicicleta) // Asegurate de que sea tu layout correcto

        val radioParcial = findViewById<RadioButton>(R.id.radioParcial)
        val radioTotal = findViewById<RadioButton>(R.id.radioTotal)
        val btnConfirmar = findViewById<Button>(R.id.btnConfirmar)
        val backArrow = findViewById<ImageView>(R.id.backArrow)

        // Asegurar que solo uno esté seleccionado
        radioParcial.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) radioTotal.isChecked = false
        }

        radioTotal.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) radioParcial.isChecked = false
        }

        // Acción al confirmar
        btnConfirmar.setOnClickListener {
            val idGenerado = Random.nextInt(1000, 9999)

            when {
                radioParcial.isChecked -> {
                    mostrarAlertaExito("Plan Parcial", idGenerado)
                }
                radioTotal.isChecked -> {
                    mostrarAlertaExito("Plan Total", idGenerado)
                }
                else -> {
                    Toast.makeText(this, "Por favor, seleccioná un plan antes de continuar.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Flecha para volver
        backArrow.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Función para mostrar alerta de éxito
    private fun mostrarAlertaExito(plan: String, id: Int) {
        AlertDialog.Builder(this)
            .setTitle("¡Éxito!")
            .setMessage("Pack '$plan' seleccionado correctamente.\nID: $id")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                // Redirigir al menú (MainActivity)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()  // Opcional: cerrar esta actividad para que no quede en el historial
            }
            .show()
    }
}

