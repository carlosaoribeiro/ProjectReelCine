package com.carlosribeiro.reelcineproject.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.MotionEvent
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import com.carlosribeiro.reelcineproject.databinding.ActivityLoginBinding
import com.carlosribeiro.reelcineproject.util.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // 🎨 Gradiente no título "ReelCine"
        binding.textTitulo.applyGradientText("#38BDF8", "#A855F7")

        // ✨ Animação de entrada do título
        binding.textTitulo.alpha = 0f
        binding.textTitulo.translationY = 40f
        binding.textTitulo.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(800)
            .setStartDelay(100)
            .start()

        // 💫 Microanimação no botão principal (toque)
        binding.btnEntrar.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN ->
                    v.animate().scaleX(0.96f).scaleY(0.96f).setDuration(100).start()
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL ->
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
            }
            false
        }

        // ✨ Animação de entrada dos campos e botão
        listOf(
            binding.emailLayout,
            binding.senhaLayout,
            binding.btnEntrar,
            binding.textEsqueciSenha,
            binding.linkCadastro
        ).forEachIndexed { index, view ->
            view.alpha = 0f
            view.translationY = 40f
            view.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(700)
                .setStartDelay((200L * (index + 1)))
                .start()
        }

        // 🔗 Links e login
        binding.linkCadastro.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }

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

        binding.btnEntrar.setOnClickListener { loginUser() }
    }

    // 🧩 Função utilitária para aplicar gradiente no texto
    private fun TextView.applyGradientText(startColor: String, endColor: String) {
        val paint = this.paint
        val width = paint.measureText(this.text.toString())
        val shader = LinearGradient(
            0f, 0f, width, this.textSize,
            intArrayOf(startColor.toColorInt(), endColor.toColorInt()),
            null,
            Shader.TileMode.CLAMP
        )
        this.paint.shader = shader
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
