package com.example.firebase01_23_01_23

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.firebase01_23_01_23.databinding.ActivityMainBinding
import com.example.firebase01_23_01_23.prefs.Prefs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var email = ""
    private var password = ""
    lateinit var prefs: Prefs
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefs = Prefs(this)
        comprobarSesion()
        setListeners()
    }
//--------------------------------------------
    private fun setListeners() {
        binding.btnRegistrar.setOnClickListener { registrar() }
        binding.btnLogin.setOnClickListener { login() }
    }
//--------------------------------------------
    private fun login() {

    }
//--------------------------------------------
    private fun registrar() {
        if (!recogerDatos()) return
        //Suponemos que el usuario se ha registrado correctamente
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                prefs.guardarEmail(email)
                irHome()
            } else {
                mostrarMensaje("Error al registrar")
            }
        }

    }

    private fun mostrarMensaje(msg: String) {
        val builder = AlertDialog.Builder(this)
            .setTitle("Mensaje")
            .setMessage(msg)
            .setPositiveButton("Aceptar", null)
            .create()
            .show()
    }

    private fun recogerDatos(): Boolean {
        email = binding.etMail.text.toString().trim()
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            //El email no es válido
            binding.etMail.error = "El email no es válido"
            return false
        }
        password = binding.etPass.text.toString()
        if (password.length < 5) {
            binding.etPass.error = "La contraseña debe tener al menos 5 caracteres"
            return false
        }
        return true
    }

    //--------------------------------------------
    private fun comprobarSesion() {
        val e = prefs.leerEmail()
        if (!e.isNullOrEmpty()) {
            irHome()
        } else {
            // Hay email guardado
        }
    }

    private fun irHome() {
        startActivity(Intent(this, HomeActivity::class.java))
    }
//--------------------------------------------
}