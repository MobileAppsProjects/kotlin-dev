package com.example.moviles

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    private val SPLASH_DISPLAY_LENGTH = 3000 // Tiempo en milisegundos (3 segundos)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Espera durante un tiempo antes de iniciar la actividad principal
        Handler().postDelayed({
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
            // Cierra esta actividad para que el usuario no pueda volver a ella presionando el botón Atrás
            finish()
        }, SPLASH_DISPLAY_LENGTH.toLong())
    }
}
