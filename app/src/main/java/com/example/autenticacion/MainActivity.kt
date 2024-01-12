package com.example.autenticacion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.autenticacion.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    public lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bIniciarSesion.setOnClickListener {
            // Al pulsar sobre el boton INICIAR SESION, comprobamos autentificacion
            // pasandole a FireBase el correo y la contraseña introducida
            login()
        }

        binding.bRegistrarse.setOnClickListener {
            // Al pulsar sobre el boton REGISTRAR, pasamos a la pantalla RegistroActivity
            startActivity(Intent(this, RegistroActivity::class.java))
        }
    }

    private fun login() {
        // Si el correo y el password no son campos vacios:
        if(binding.email.text.isNotEmpty() && binding.password.text.isNotEmpty()) {
            // Iniciamos sesion con el metodo signIn y enviamos a Firebase el correo y la contraseña
            FirebaseAuth.getInstance().signInWithEmailAndPassword(
                binding.email.text.toString(),
                binding.password.text.toString()
            )
                .addOnCompleteListener {
                    // Si la autentificacion tuvo exito:
                    if(it.isSuccessful) {
                        // Accedemos a la pantalla InicioActivity, para dar la bienvenida al usuario
                        val intent = Intent(this, InicioActivity::class.java)
                    } else {
                        // sino avisamos al usuario que ocurrio un problema
                        Toast.makeText(this, "Correo o password incorrecto", Toast.LENGTH_SHORT).show()
                    }
                }
        } else { Toast.makeText(this, "Algun campo esta vacio", Toast.LENGTH_SHORT).show() }
    }

}