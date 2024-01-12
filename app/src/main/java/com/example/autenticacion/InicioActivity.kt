package com.example.autenticacion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.autenticacion.databinding.ActivityInicioBinding

class InicioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = FireBaseFirestore.getInstance()

        val extras = intent.extras
        val nombre = extras?.getString("nombreusuario")
        binding.bienvenido.text="Bienvenid@, $nombre"

        binding.bCerrarSesion.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            // Volvemos a nuestra MainActivity:
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.bGuardar.setOnClickListener {
            // Si ningun campo esta vacio:
            if( binding.marca.text.isNotEmpty() && binding.modelo.text.isNotEmpty() &&
                binding.matricula.text.isNotEmpty() && binding.color.text.isNotEmpty()) {
                db.collection("coches").document(binding.matricula.text.toString())
                    // Al pulsar sobre el botón
                    //  COCHE, se añadirá un nuevo documento (registro de la tabla coches) llamando
                    // al método set(), que espera un mapa hash como argumento:
                    .set(mapOf(
                        "color" to binding.color.text.toString(),
                        "marca" to binding.marca.text.toString(),
                        "modelo" to binding.modelo.text.toString()
                    )
                    )

            } else { Toast.makeText(this, "Algun campo esta vacio", Toast.LENGTH_SHORT).show() }
        }

        binding.bEditar.setOnClickListener {
            db.collection("coches")
                // La API Firestore proporciona el método filtro whereEqualTo() para buscar un valor
                // dado en una colección. Debido a que este método puede devolver múltiples documentos
                // (registros) como resultado, necesitarás un ciclo dentro de tu OnSuccessListener para
                // manejar cada resultado:
                .whereEqualTo("matricula", binding.matricula.text.toString())
                .get().addOnSuccessListener {
                    it.forEach {
                        binding.marca.setText(it.get("marca") as String?)
                        binding.modelo.setText(it.get("modelo") as String?)
                        binding.color.setText(it.get("color") as String?)
                    }
                }
        }

        binding.bEliminar.setOnClickListener {
            // Eliminar un coche, dada la matricula:
            db.collection("coches")
                .document(binding.matricula.text.toString())
                .delete()
        }

    }
}