package com.example.firebase01_23_01_23

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import com.example.firebase01_23_01_23.databinding.ActivityCrearBinding

class CrearActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCrearBinding
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
    }

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

    }

    private fun pintarEdad() {
        binding.tvEdad2.text = String.format(getString(R.string.edad), edad)

    }
}