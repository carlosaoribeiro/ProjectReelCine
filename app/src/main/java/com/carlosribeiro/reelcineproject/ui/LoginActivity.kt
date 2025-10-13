package com.carlosribeiro.reelcineproject.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import com.carlosribeiro.reelcineproject.databinding.ActivityLoginBinding
import com.carlosribeiro.reelcineproject.util.SessionManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.carlosribeiro.reelcineproject.R
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // 🎨 Gradiente no título
        binding.textTitulo.applyGradientText("#38BDF8", "#A855F7")

        // ✨ Animações iniciais
        animateIntro()

        // 🔗 Navegação
        binding.linkCadastro.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }

        binding.textEsqueciSenha.setOnClickListener { sendResetEmail() }

        binding.btnEntrar.setOnClickListener { loginUser() }

        // ☁️ Configuração correta do Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            // ⚠️ Usa o CLIENT_ID WEB do google-services.json (não o tipo 3)
            .requestIdToken(getString(R.string.default_web_client_id))

            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // 🔘 Clique no botão "Continuar com Google"
        binding.btnGoogle.setOnClickListener { signInWithGoogle() }
    }

    // 🌈 Gradiente no texto
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

    // 🪄 Animações suaves de entrada
    private fun animateIntro() {
        binding.textTitulo.alpha = 0f
        binding.textTitulo.translationY = 40f
        binding.textTitulo.animate().alpha(1f).translationY(0f).setDuration(800).setStartDelay(100).start()

        listOf(
            binding.emailLayout,
            binding.senhaLayout,
            binding.btnEntrar,
            binding.textEsqueciSenha,
            binding.linkCadastro,
            binding.btnGoogle
        ).forEachIndexed { index, view ->
            view.alpha = 0f
            view.translationY = 40f
            view.animate().alpha(1f).translationY(0f).setDuration(700).setStartDelay((200L * (index + 1))).start()
        }

        // Efeito de toque no botão principal
        binding.btnEntrar.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> v.animate().scaleX(0.96f).scaleY(0.96f).setDuration(100).start()
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> v.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
            }
            false
        }
    }

    // 📧 Envio de e-mail de redefinição de senha
    private fun sendResetEmail() {
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

    // 🚀 Login com conta Google → Firebase
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser ?: return@addOnCompleteListener

                    val nome = user.displayName ?: "Usuário"
                    val email = user.email ?: ""
                    val uid = user.uid

                    // Salva dados no Firestore
                    FirebaseFirestore.getInstance().collection("usuarios")
                        .document(uid)
                        .set(mapOf("nome" to nome, "email" to email))
                        .addOnSuccessListener {
                            SessionManager(this).saveUser(nome, email, uid)
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Erro ao salvar dados do usuário", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Falha na autenticação com Google.", Toast.LENGTH_SHORT).show()
                    Log.e("LoginActivity", "Google Auth Error", task.exception)
                }
            }
    }

    // 🌐 Fluxo de login com Google
    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    // 🧭 Resultado da intent do Google
    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null) firebaseAuthWithGoogle(account)
        } catch (e: ApiException) {
            Log.e("LoginActivity", "Erro no login Google: ${e.statusCode}")
            Toast.makeText(this, "Erro ao entrar com o Google", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        auth.currentUser?.let {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    // 📩 Login com e-mail e senha tradicional
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
                        FirebaseFirestore.getInstance().collection("usuarios").document(userId)
                            .get()
                            .addOnSuccessListener { doc ->
                                val nome = doc.getString("nome") ?: "Usuário"
                                val emailUser = auth.currentUser?.email ?: ""
                                SessionManager(this).saveUser(nome, emailUser, userId)
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Erro ao carregar perfil", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(this, "Falha no login: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
