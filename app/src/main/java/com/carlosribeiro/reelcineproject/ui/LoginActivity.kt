package com.carlosribeiro.reelcineproject.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.carlosribeiro.reelcineproject.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa o Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Botão de login
        binding.btnEntrar.setOnClickListener {
            loginUser()
        }

        // Link para tela de cadastro
        binding.textCadastrar.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }

        // Link para esqueci minha senha
        binding.textEsqueciSenha.setOnClickListener {
            Toast.makeText(this, "Função 'Esqueci minha senha' ainda não implementada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loginUser() {
        val email = binding.editEmail.text.toString().trim()
        val password = binding.editSenha.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, FeedActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Falha no login: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
