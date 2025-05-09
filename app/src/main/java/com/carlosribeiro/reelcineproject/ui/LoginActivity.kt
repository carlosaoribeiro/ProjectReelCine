package com.carlosribeiro.reelcineproject.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.carlosribeiro.reelcineproject.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerTextView: TextView
    private lateinit var forgotPasswordTextView: TextView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializar o FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Encontrar os componentes da UI
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        registerTextView = findViewById(R.id.registerTextView)
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView)

        // Ação do botão de login
        loginButton.setOnClickListener {
            loginUser()
        }

        // Ação do link para cadastro
        registerTextView.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
        }

        // Ação do link para esqueci minha senha
        forgotPasswordTextView.setOnClickListener {
            // Aqui você pode adicionar a lógica para recuperação de senha
            Toast.makeText(this, "Função 'Esqueci minha senha' ainda não implementada", Toast.LENGTH_SHORT).show()
        }
    }

    // Função para login com Firebase Authentication
    private fun loginUser() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            return
        }

        // Fazer login com o Firebase
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Se o login for bem-sucedido, redireciona para a tela principal
                    val intent = Intent(this, FeedActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Se o login falhar, exibe uma mensagem de erro
                    Toast.makeText(this, "Falha no login: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
