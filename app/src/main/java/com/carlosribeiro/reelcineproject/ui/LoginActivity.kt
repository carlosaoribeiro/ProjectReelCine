package com.carlosribeiro.reelcineproject.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.carlosribeiro.reelcineproject.databinding.ActivityLoginBinding
import com.carlosribeiro.reelcineproject.util.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Login
        binding.btnEntrar.setOnClickListener {
            loginUser()
        }

        // Link para cadastro
        binding.linkCadastro.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }

        // Link para redefinição de senha
        binding.textEsqueciSenha.setOnClickListener {
            val email = binding.editEmail.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Digite seu e-mail para redefinir a senha", Toast.LENGTH_SHORT).show()
            } else {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "E-mail de redefinição enviado!", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this, "Erro: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val usuarioLogado = auth.currentUser
        if (usuarioLogado != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
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
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        FirebaseFirestore.getInstance()
                            .collection("usuarios")
                            .document(userId)
                            .get()
                            .addOnSuccessListener { document ->
                                val nome = document.getString("nome") ?: "Usuário"
                                val emailUser = auth.currentUser?.email ?: ""
                                val uid = auth.currentUser?.uid ?: ""

                                SessionManager(this).saveUser(nome, emailUser, uid)

                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Erro ao carregar perfil", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Falha no login: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
