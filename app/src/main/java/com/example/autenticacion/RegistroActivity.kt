package com.example.autenticacion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.autenticacion.databinding.ActivityRegistroBinding

class RegistroActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegistroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Nuevo Usuario" // Cambiar el titulo de la pantalla

        val db = FireBaseFirestore.getInstance()

        binding.bRegistrarse2.setOnClickListener {
            // Comprobamos que ningun campo este vacio:
            if (binding.email2.text.isNotEmpty() && binding.password2.text.isNotEmpty()
                && binding.nombre.text.isNotEmpty() && binding.apellidos.text.isNotEmpty()
            ) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.email2.text.toString(), binding.password2.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) { // si se han registrado los datos satisfactoriamente:

                        // Añadimos todos los datos del usuario en Firestore:
                        db.collection("usuarios").document(binding.email2.text.toString())
                            // Al pulsar sobre el botón
                            //  COCHE, se añadirá un nuevo documento (registro de la tabla coches) llamando
                            // al método set(), que espera un mapa hash como argumento:
                            .set(mapOf(
                                "nombre" to binding.nombre.text.toString(),
                                "apellidos" to binding.apellidos.text.toString(),
                            )
                            )

                        // Accedemos a la apantalla InicioActivity para dar la bienvenida al usuario
                        val intent = Intent(this, InicioActivity::class.java).apply {
                            putExtra("nombreusuario", binding.nombre.text.toString())
                        }
                        startActivity(intent)
                    } else { Toast.makeText(this, "Error en el registro del nuevo usuario", Toast.LENGTH_SHORT).show() }
                }
            } else { Toast.makeText(this, "Algun campo esta vacio", Toast.LENGTH_SHORT).show() }
        }
    }
}