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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.lifecycle.lifecycleScope

import com.carlosribeiro.reelcineproject.R
import com.carlosribeiro.reelcineproject.databinding.ActivityLoginBinding
import com.carlosribeiro.reelcineproject.util.SessionManager

import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.CustomCredential
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption

import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.textTitulo.applyGradientText("#38BDF8", "#A855F7")
        animateIntro()

        binding.linkCadastro.setOnClickListener { startActivity(Intent(this, CadastroActivity::class.java)) }
        binding.textEsqueciSenha.setOnClickListener { sendResetEmail() }
        binding.btnEntrar.setOnClickListener { loginUser() }
        binding.btnGoogle.setOnClickListener { signInWithGoogle() }
    }

    // ## NOVA LÓGICA DE LOGIN COM GOOGLE (Credential Manager) ##
    private fun signInWithGoogle() {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(getString(R.string.default_web_client_id)) // Web client ID (OAuth 2.0)
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val credentialManager = CredentialManager.create(this)

        lifecycleScope.launch {
            try {
                val result = credentialManager.getCredential(
                    context = this@LoginActivity,
                    request = request
                )

                val credential = result.credential
                if (credential is CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {
                    try {
                        val googleIdTokenCred = GoogleIdTokenCredential.createFrom(credential.data)
                        val idToken = googleIdTokenCred.idToken
                        if (!idToken.isNullOrEmpty()) {
                            firebaseAuthWithGoogle(idToken)
                        } else {
                            Toast.makeText(this@LoginActivity, "Token do Google ausente.", Toast.LENGTH_SHORT).show()
                            Log.e("LoginActivity", "Google ID token nulo/vazio")
                        }
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e("LoginActivity", "Falha ao parsear GoogleIdToken", e)
                        Toast.makeText(this@LoginActivity, "Erro ao processar login com Google.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Credencial inesperada.", Toast.LENGTH_SHORT).show()
                    Log.e("LoginActivity", "Tipo de credencial inválido: ${credential::class.java.name}")
                }
            } catch (e: GetCredentialException) {
                Log.e("LoginActivity", "Falha ao obter credencial: ${e.message}", e)
                Toast.makeText(this@LoginActivity, "Erro ao iniciar login com Google.", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("LoginActivity", "Erro inesperado no login Google", e)
                Toast.makeText(this@LoginActivity, "Erro ao processar login com Google.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ## FUNÇÃO ATUALIZADA para receber o idToken diretamente ##
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser ?: return@addOnCompleteListener
                    val nome = user.displayName ?: "Usuário"
                    val email = user.email ?: ""
                    val uid = user.uid

                    val userMap = mapOf(
                        "nome" to nome,
                        "email" to email,
                        "avatarUrl" to (user.photoUrl?.toString() ?: "")
                    )

                    FirebaseFirestore.getInstance()
                        .collection("usuarios")
                        .document(uid)
                        .set(userMap)
                        .addOnSuccessListener {
                            SessionManager(this).saveUser(nome, email, uid)
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Erro ao salvar dados do usuário.", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Falha na autenticação com Google.", Toast.LENGTH_SHORT).show()
                    Log.e("LoginActivity", "Google Auth Error", task.exception)
                }
            }
    }

    // --- RESTANTE DO SEU CÓDIGO ---
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

    private fun animateIntro() {
        binding.textTitulo.alpha = 0f
        binding.textTitulo.translationY = 40f
        binding.textTitulo.animate().alpha(1f).translationY(0f).setDuration(800).setStartDelay(100).start()
        listOf(binding.emailLayout, binding.senhaLayout, binding.btnEntrar, binding.textEsqueciSenha, binding.linkCadastro, binding.btnGoogle)
            .forEachIndexed { index, view ->
                view.alpha = 0f
                view.translationY = 40f
                view.animate().alpha(1f).translationY(0f).setDuration(700).setStartDelay((200L * (index + 1))).start()
            }
        binding.btnEntrar.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> v.animate().scaleX(0.96f).scaleY(0.96f).setDuration(100).start()
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> v.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
            }
            false
        }
    }

    private fun sendResetEmail() {
        val email = binding.editEmail.text.toString().trim()
        if (email.isEmpty()) {
            Toast.makeText(this, "Digite seu e-mail para redefinir a senha", Toast.LENGTH_SHORT).show()
        } else {
            auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "E-mail de redefinição enviado!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Erro: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        auth.currentUser?.let {
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
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val userId = auth.currentUser?.uid
                if (userId != null) {
                    FirebaseFirestore.getInstance().collection("usuarios").document(userId).get()
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
