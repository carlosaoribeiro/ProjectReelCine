package com.carlosribeiro.reelcineproject.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        carregarDados()

        binding.imageAvatar.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (cameraIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
            }
        }

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
