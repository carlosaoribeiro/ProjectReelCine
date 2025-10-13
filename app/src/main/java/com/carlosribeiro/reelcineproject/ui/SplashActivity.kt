package com.carlosribeiro.reelcineproject.ui

import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.carlosribeiro.reelcineproject.databinding.ActivitySplashBinding
import com.google.firebase.auth.FirebaseAuth
import com.carlosribeiro.reelcineproject.R // mantém o import correto

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val splashDelay = 5000L // 2 segundos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lottieReel.apply {
            alpha = 0f
            scaleX = 0.8f
            scaleY = 0.8f
            animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(1200)
                .setInterpolator(android.view.animation.OvershootInterpolator())
                .start()
        }

        binding.lottieReel.apply {
            speed = 0.5f // deixa a animação rodando mais lentamente
            playAnimation()
        }

        // Inicia a animação da Lottie (caso não esteja com autoplay)
        binding.lottieReel.playAnimation()

        // Ativa o gradiente animado no texto
        animateGradientText()

        // Handler para redirecionar após o splash
        Handler(Looper.getMainLooper()).postDelayed({
            val user = FirebaseAuth.getInstance().currentUser
            val intent = if (user != null) {
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, LoginActivity::class.java)
            }

            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }, splashDelay)
    }

    // Função que cria o efeito de gradiente animado no texto "ReelCine"
    private fun animateGradientText() {
        val textView = binding.textAppName

        textView.post {
            val width = textView.width.toFloat()

            val paint = textView.paint
            val gradient = LinearGradient(
                0f, 0f, width, 0f,
                intArrayOf(
                    0xFF00BFFF.toInt(), // azul neon
                    0xFFA855F7.toInt(), // roxo
                    0xFF00BFFF.toInt()  // volta ao azul
                ),
                null,
                Shader.TileMode.CLAMP
            )

            paint.shader = gradient

            val animator = ValueAnimator.ofFloat(0f, width * 2)
            animator.duration = 4000L
            animator.repeatCount = ValueAnimator.INFINITE
            animator.addUpdateListener {
                val animatedValue = it.animatedValue as Float
                val shader = LinearGradient(
                    animatedValue, 0f, animatedValue + width, 0f,
                    intArrayOf(
                        0xFF00BFFF.toInt(),
                        0xFFA855F7.toInt(),
                        0xFF00BFFF.toInt()
                    ),
                    null,
                    Shader.TileMode.CLAMP
                )
                paint.shader = shader
                textView.invalidate()
            }

            animator.start()
        }
    }
}
