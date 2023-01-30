package com.example.firebase01_23_01_23

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import com.example.firebase01_23_01_23.databinding.ActivityCrearBinding
import com.example.firebase01_23_01_23.models.Usuarios
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.nio.channels.spi.AbstractSelectionKey

class CrearActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCrearBinding

    private var db = Firebase.database

    private var editar = false

    var username = ""
    var edad = 18
    var nombre = ""
    var apellidos = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pintarEdad()
        setListeners()
        cogerUsuario()
    }
//-------------------------------------------------------------------------------------------------
    private fun cogerUsuario() {
        val usuario: Usuarios = getSerializable(intent, "usuario", Usuarios::class.java)
    }
//-------------------------------------------------------------------------------------------------
    private fun setListeners() {
        binding.btnCancelar.setOnClickListener {
            finish()
        }
        binding.btnAceptar.setOnClickListener {
            guardarUsuario()
        }
        binding.seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                edad = progress+18
                pintarEdad()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
    }

    private fun guardarUsuario() {
        if (errorEnDatos()) return
        //Los campos no estan vacios, vamos a guardar el usuario en realtime database
        val usuario = Usuarios(nombre, apellidos, edad, username)
        db = FirebaseDatabase.getInstance("https://fir-2023-78680-default-rtdb.europe-west1.firebasedatabase.app/")
        val ref = db.getReference("usuarios")
        ref.child(username).setValue(usuario).addOnSuccessListener {
            finish()
        }
    }

    private fun errorEnDatos(): Boolean {
        username = binding.etUserName.text.toString().trim()
        if (username.length < 3) {
            binding.etUserName.error = "El nombre de usuario debe tener al menos 3 caracteres"
            return true
        }
        nombre = binding.etNombre.text.toString().trim()
        if (nombre.length < 3) {
            binding.etNombre.error = "El nombre debe tener al menos 3 caracteres"
            return true
        }
        apellidos = binding.etApellidos.text.toString().trim()
        if (apellidos.length < 3) {
            binding.etApellidos.error = "Los apellidos deben tener al menos 3 caracteres"
            return true
        }
        return false
    }

    private fun pintarEdad() {
        binding.tvEdad2.text = String.format(getString(R.string.edad), edad)

    }
}

private fun <T: java.io.Serializable?> getSerializable(intent: Intent, key: String, clase: Class<T>): T {
    return if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.TIRAMISU) {
        intent.getSerializableExtra(key, clase)!!
    } else {
        intent.getSerializableExtra(key) as T
    }

}
