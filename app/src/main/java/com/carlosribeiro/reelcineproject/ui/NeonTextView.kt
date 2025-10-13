package com.carlosribeiro.reelcineproject.ui.components

import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Shader
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.carlosribeiro.reelcineproject.R

class NeonTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var gradient: LinearGradient? = null
    private val matrixAnim = Matrix()
    private var translateX = 0f

    private val gradientSpeed = 8f // controle da velocidade

    init {
        paint.apply {
            isAntiAlias = true
            style = Paint.Style.FILL
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0) {
            val startColor = ContextCompat.getColor(context, R.color.teal_200)
            val midColor = ContextCompat.getColor(context, R.color.reelcine_gradient)
            val endColor = ContextCompat.getColor(context, R.color.teal_200)

            gradient = LinearGradient(
                0f, 0f, w.toFloat(), 0f,
                intArrayOf(startColor, midColor, endColor),
                floatArrayOf(0f, 0.5f, 1f),
                Shader.TileMode.CLAMP
            )

            paint.shader = gradient
        }
    }

    override fun onDraw(canvas: android.graphics.Canvas) {
        super.onDraw(canvas)
        gradient?.let {
            translateX += gradientSpeed
            val width = width.toFloat()
            if (translateX > width * 2) translateX = 0f
            matrixAnim.setTranslate(translateX, 0f)
            it.setLocalMatrix(matrixAnim)
            invalidate()
        }
    }
}
