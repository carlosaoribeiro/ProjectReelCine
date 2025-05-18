package com.carlosribeiro.reelcineproject.ui


import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager // ← IMPORTAÇÃO FALTANTE
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.carlosribeiro.reelcineproject.databinding.ActivityPerfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class PerfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPerfilBinding
    private val uid = FirebaseAuth.getInstance().currentUser?.uid
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private var novaFoto: Bitmap? = null

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 101
        private const val REQUEST_CAMERA_PERMISSION = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        carregarDados()

        // Verificar permissão de câmera ao clicar na imagem do avatar
        binding.imageAvatar.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
                // Solicitar permissão caso ainda não tenha sido concedida
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
            } else {
                // Caso já tenha permissão, abre a câmera
                abrirCamera()
            }
        }

        // Ao salvar perfil
        binding.btnSalvar.setOnClickListener {
            val nome = binding.editNome.text.toString().trim()
            val email = binding.editEmail.text.toString().trim()

            if (uid == null || nome.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Campos obrigatórios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (novaFoto != null) {
                uploadAvatar(novaFoto!!) { avatarUrl ->
                    salvarUsuario(nome, email, avatarUrl)
                }
            } else {
                salvarUsuario(nome, email, null)
            }
        }
    }

    // Função para verificar permissões ao iniciar a câmera
    private fun abrirCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
        } else {
            Toast.makeText(this, "Câmera não disponível", Toast.LENGTH_SHORT).show()
        }
    }

    // Carregar dados do usuário (nome, e-mail, avatar)
    private fun carregarDados() {
        uid?.let {
            firestore.collection("usuarios").document(it).get()
                .addOnSuccessListener { doc ->
                    binding.editNome.setText(doc.getString("nome") ?: "")
                    binding.editEmail.setText(doc.getString("email") ?: "")

                    val avatarUrl = doc.getString("avatarUrl")
                    if (!avatarUrl.isNullOrBlank()) {
                        Glide.with(this)
                            .load(avatarUrl)
                            .circleCrop()
                            .into(binding.imageAvatar)
                    }
                }
        }
    }

    // Função para salvar usuário no Firestore
    private fun salvarUsuario(nome: String, email: String, avatarUrl: String?) {
        val userMap = hashMapOf(
            "nome" to nome,
            "email" to email
        )
        if (avatarUrl != null) {
            userMap["avatarUrl"] = avatarUrl
        }

        uid?.let {
            firestore.collection("usuarios").document(it)
                .update(userMap as Map<String, Any>)
                .addOnSuccessListener {
                    Toast.makeText(this, "Perfil atualizado!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erro ao salvar perfil", Toast.LENGTH_SHORT).show()
                }
        }
    }

    // Função para upload da foto no Firebase Storage
    private fun uploadAvatar(bitmap: Bitmap, callback: (String) -> Unit) {
        val ref = storage.reference.child("avatars/$uid.jpg")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        ref.putBytes(data)
            .continueWithTask { ref.downloadUrl }
            .addOnSuccessListener { uri ->
                callback(uri.toString())
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao salvar foto", Toast.LENGTH_SHORT).show()
            }
    }

    // Tratar a resposta de permissões de câmera
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                abrirCamera()
            } else {
                Toast.makeText(this, "Permissão de câmera negada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Lidar com a foto capturada pela câmera
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val foto = data?.extras?.get("data") as? Bitmap
            foto?.let {
                novaFoto = it
                binding.imageAvatar.setImageBitmap(it)
            }
        }
    }
}
