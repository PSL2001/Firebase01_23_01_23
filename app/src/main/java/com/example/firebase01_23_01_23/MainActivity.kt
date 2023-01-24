package com.example.firebase01_23_01_23

import android.app.Instrumentation.ActivityResult
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.firebase01_23_01_23.databinding.ActivityMainBinding
import com.example.firebase01_23_01_23.prefs.Prefs
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private val responseLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            val tarea = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val cuenta = tarea.getResult(ApiException::class.java) //Aqui se guarda la cuenta que me valido
                if (cuenta != null) {
                    val credenciales = GoogleAuthProvider.getCredential(cuenta.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credenciales).addOnCompleteListener() {
                        if (it.isSuccessful) {
                            prefs.guardarEmail(cuenta.email ?: "")
                            irHome()
                        } else {
                            mostrarMensaje("Error al autenticar con Google")
                        }
                    }
                }
            } catch (e: Exception) {
                println(e.message.toString())
            }
        }
    }


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
        binding.btnGoogle.setOnClickListener { registroGoogle() }
    }
//--------------------------------------------
    private fun registroGoogle() {
        val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("166281049177-60io4t1qs4jlhcssda8fldksused2t19.apps.googleusercontent.com")
            .requestEmail()
            .build()
        val googleClient = GoogleSignIn.getClient(this, googleConf)
        // Para que si cierro sesion me da a eleigr un usuario y no me valido con el ultimo
        googleClient.signOut()
        responseLauncher.launch(googleClient.signInIntent)
    }

    //--------------------------------------------
    private fun login() {
        if (!recogerDatos()) return
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                prefs.guardarEmail(email)
                irHome()
            } else {
                mostrarMensaje("Error al iniciar sesión")
            }
        }
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