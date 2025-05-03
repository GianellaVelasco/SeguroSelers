package com.example.selersmobile

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var nombreInput: EditText
    private lateinit var apellidoInput: EditText
    private lateinit var correoInput: EditText
    private lateinit var contraseñaInput: EditText
    private lateinit var confirmarContraseñaInput: EditText
    private lateinit var crearCuentaButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        nombreInput = findViewById(R.id.nombre_input)
        apellidoInput = findViewById(R.id.apellido_input)
        correoInput = findViewById(R.id.correo_input)
        contraseñaInput = findViewById(R.id.contraseña_input)
        confirmarContraseñaInput = findViewById(R.id.confirmar_contraseña_input)
        crearCuentaButton = findViewById(R.id.crear_cuenta_button)

        // Acción para crear cuenta
        crearCuentaButton.setOnClickListener {
            val nombre = nombreInput.text.toString().trim()
            val apellido = apellidoInput.text.toString().trim()
            val correo = correoInput.text.toString().trim()
            val contraseña = contraseñaInput.text.toString()
            val confirmarContraseña = confirmarContraseñaInput.text.toString()

            // Validar datos
            if (validarDatos(nombre, apellido, correo, contraseña, confirmarContraseña)) {
                // Si los datos son válidos, puedes proceder con el registro (por ejemplo, guardar en la base de datos)
                Toast.makeText(this, "Cuenta creada con éxito", Toast.LENGTH_SHORT).show()

                // Redirigir al LoginActivity después de un registro exitoso
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish() // Cierra esta actividad
            }
        }
        val irALoginTextView = findViewById<TextView>(R.id.ir_a_login)

        irALoginTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Opcional: cierra la pantalla actual para que no pueda volver con el botón "Atrás"
        }

    }

    // Función para validar los datos del formulario
    private fun validarDatos(
        nombre: String,
        apellido: String,
        correo: String,
        contraseña: String,
        confirmarContraseña: String
    ): Boolean {
        // Validar nombre
        if (nombre.isEmpty()) {
            nombreInput.error = "El nombre es obligatorio"
            nombreInput.requestFocus()
            return false
        }

        // Validar apellido
        if (apellido.isEmpty()) {
            apellidoInput.error = "El apellido es obligatorio"
            apellidoInput.requestFocus()
            return false
        }

        // Validar correo
        if (correo.isEmpty()) {
            correoInput.error = "El correo es obligatorio"
            correoInput.requestFocus()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            correoInput.error = "Correo inválido"
            correoInput.requestFocus()
            return false
        }

        // Validar contraseña
        if (contraseña.isEmpty()) {
            contraseñaInput.error = "La contraseña es obligatoria"
            contraseñaInput.requestFocus()
            return false
        }
        if (contraseña.length < 6) {
            contraseñaInput.error = "Mínimo 6 caracteres"
            contraseñaInput.requestFocus()
            return false
        }

        // Validar confirmación de la contraseña
        if (confirmarContraseña.isEmpty()) {
            confirmarContraseñaInput.error = "Debes confirmar la contraseña"
            confirmarContraseñaInput.requestFocus()
            return false
        }
        if (confirmarContraseña != contraseña) {
            confirmarContraseñaInput.error = "Las contraseñas no coinciden"
            confirmarContraseñaInput.requestFocus()
            return false
        }

        return true
    }
}