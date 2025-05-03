package com.example.selersmobile

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.selersmobile.MainActivity
import com.example.selersmobile.R
import com.example.selersmobile.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var showPassword: ImageView
    private lateinit var loginButton: Button
    private lateinit var registerLink: TextView

    private var passwordVisible = false

    // Simulamos un usuario registrado (en la vida real esto vendría de la base de datos)
    private val dummyEmail = "usuario@correo.com"
    private val dummyPassword = "123456"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializamos las vistas
        emailInput = findViewById(R.id.email_input)
        passwordInput = findViewById(R.id.password_input)
        showPassword = findViewById(R.id.show_password)
        loginButton = findViewById(R.id.login_button)
        registerLink = findViewById(R.id.register_link)

        // Configuración del botón para mostrar/ocultar la contraseña
        showPassword.setOnClickListener {
            passwordVisible = !passwordVisible
            if (passwordVisible) {
                passwordInput.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            passwordInput.setSelection(passwordInput.text.length) // Para mantener el cursor al final
        }

        // Configuración del botón de login
        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString()

            // Validamos el login
            if (!isValidLogin(email, password)) return@setOnClickListener

            // Si las credenciales son correctas, redirigimos al Home (MainActivity)
            if (email == dummyEmail && password == dummyPassword) {
                Toast.makeText(this, "Bienvenido $email", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish() // Terminamos la actividad actual para que no se vuelva al login al presionar "atrás"
            } else {
                Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }

        // Enlace para redirigir a la pantalla de registro
        registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    // Función de validación de los campos
    private fun isValidLogin(email: String, password: String): Boolean {
        // Validación de correo
        if (email.isEmpty()) {
            emailInput.error = "El correo es obligatorio"
            emailInput.requestFocus()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.error = "Correo inválido"
            emailInput.requestFocus()
            return false
        }
        // Validación de la contraseña
        if (password.isEmpty()) {
            passwordInput.error = "La contraseña es obligatoria"
            passwordInput.requestFocus()
            return false
        }
        if (password.length < 6) {
            passwordInput.error = "Mínimo 6 caracteres"
            passwordInput.requestFocus()
            return false
        }
        return true
    }

}