package com.carlosribeiro.reelcineproject.ui

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.carlosribeiro.reelcineproject.databinding.ActivityCadastroBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private var avatarBitmap: Bitmap? = null

    // Launcher para tirar foto com a câmera
    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            avatarBitmap = bitmap
            binding.imageAvatar.setImageBitmap(bitmap)
        } else {
            Toast.makeText(this, "Nenhuma imagem capturada.", Toast.LENGTH_SHORT).show()
        }
    }

    // Launcher para pedir permissão da câmera
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            takePictureLauncher.launch(null)
        } else {
            Toast.makeText(this, "Permissão da câmera negada!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        // Clique para tirar foto
        binding.imageAvatar.setOnClickListener {
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }

        // Clique para cadastrar
        binding.btnCadastrar.setOnClickListener {
            val nome = binding.editTextNome.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim().lowercase()
            val senha = binding.editTextSenha.text.toString().trim()
            val confirmar = binding.editConfirmarSenha.text.toString().trim()

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || confirmar.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (senha != confirmar) {
                Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        userId?.let { uid ->
                            if (avatarBitmap != null) {
                                uploadAvatarToFirebase(uid, nome, email, avatarBitmap!!)
                            } else {
                                saveUserToFirestore(uid, nome, email, null, senha)
                            }
                        }
                    } else {
                        if (task.exception is FirebaseAuthUserCollisionException) {
                            Toast.makeText(this, "Este e-mail já está em uso.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Erro: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }
    }

    private fun uploadAvatarToFirebase(userId: String, nome: String, email: String, bitmap: Bitmap) {
        val storageRef = storage.reference.child("avatars/$userId.jpg")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageData = baos.toByteArray()

        storageRef.putBytes(imageData)
            .continueWithTask { task ->
                if (!task.isSuccessful) throw task.exception ?: Exception("Erro no upload")
                storageRef.downloadUrl
            }
            .addOnSuccessListener { uri ->
                val avatarUrl = uri.toString()
                saveUserToFirestore(userId, nome, email, avatarUrl, binding.editTextSenha.text.toString())
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao enviar imagem: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // 🔥 Versão ajustada com login automático
    private fun saveUserToFirestore(
        userId: String,
        nome: String,
        email: String,
        avatarUrl: String?,
        senha: String
    ) {
        val userMap = hashMapOf(
            "nome" to nome,
            "email" to email.lowercase(), // 🔥 sempre minúsculo
            "avatarUrl" to (avatarUrl ?: "")
        )

        firestore.collection("usuarios").document(userId)
            .set(userMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show()

                // 🔥 Só faz login DEPOIS que os dados forem realmente salvos
                auth.signInWithEmailAndPassword(email, senha)
                    .addOnSuccessListener {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Erro ao logar: ${e.message}", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao salvar usuário: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
