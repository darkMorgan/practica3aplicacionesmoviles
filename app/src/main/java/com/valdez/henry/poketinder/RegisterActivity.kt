package com.valdez.henry.poketinder

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RegisterActivity : AppCompatActivity() {
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtPassword2: EditText
    private lateinit var btnRegister: Button
    private lateinit var btnBackClose: FloatingActionButton
    private lateinit var btnLogin: Button
    private lateinit var sharedPreferencesRepository: SharedPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        edtPassword2 = findViewById(R.id.edtPassword2)
        btnRegister = findViewById(R.id.btnRegister)
        btnBackClose = findViewById(R.id.btnBackClose)
        btnLogin = findViewById(R.id.btnLogin) // Asegúrate de que el ID sea correcto
        sharedPreferencesRepository = SharedPreferencesRepository()
        sharedPreferencesRepository.setSharedPreference(this)

        // Configuración del botón de "BackNavigation"
        btnBackClose.setOnClickListener {
            finish() // Regresa a la actividad anterior
        }

        // Configuración del botón "Ya tengo una cuenta"
        btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Validaciones de campo
        edtEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
                if (s.toString().matches(emailPattern.toRegex())) {
                    edtEmail.error = null
                } else {
                    edtEmail.error = "Formato de correo inválido"
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        edtPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.length < 8) {
                    edtPassword.error = "La contraseña debe tener al menos 8 caracteres"
                } else {
                    edtPassword.error = null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        edtPassword2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != edtPassword.text.toString()) {
                    edtPassword2.error = "Las contraseñas no coinciden"
                } else {
                    edtPassword2.error = null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        btnRegister.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            val confirmPassword = edtPassword2.text.toString()

            // Validación final antes de registrar
            if (email.isNotEmpty() && password.isNotEmpty() && password == confirmPassword) {
                sharedPreferencesRepository.saveUserEmail(email)
                sharedPreferencesRepository.saveUserPassword(password)

                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()

                // Redirigir a LoginActivity y finalizar RegisterActivity
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish() // Cierra RegisterActivity para que el usuario no regrese con el botón de retroceso
            } else {
                Toast.makeText(this, "Verifica los datos ingresados", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
