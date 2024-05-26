package com.example.restaurantapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.restaurantapp.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LogOutActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logout)

        firebaseAuth = FirebaseAuth.getInstance()

        // Configurar un clic de listener para el botón de cierre de sesión
        findViewById<View>(R.id.logoutButton).setOnClickListener {
            logoutUser()
        }
    }

    private fun logoutUser() {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                firebaseAuth.signOut()
            }
            // Limpiar preferencias u otros datos de usuario si es necesario
            PreferenceManager.setLoggedIn(this@LogOutActivity, false)

            // Redirigir al usuario a la pantalla de inicio de sesión
            val intent = Intent(this@LogOutActivity, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}