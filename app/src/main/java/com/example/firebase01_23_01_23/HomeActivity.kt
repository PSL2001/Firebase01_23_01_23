package com.example.firebase01_23_01_23

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.firebase01_23_01_23.databinding.ActivityHomeBinding
import com.example.firebase01_23_01_23.prefs.Prefs
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    private lateinit var prefs: Prefs
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefs = Prefs(this)
        setListeners()

    }

    private fun setListeners() {
        binding.btnAdd.setOnClickListener {
            startActivity(Intent(this, CrearActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_option, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_logout -> {
                FirebaseAuth.getInstance().signOut()
                prefs.borradoTotal()
                finish()
                true
            }
            R.id.item_exit -> {
                finishAffinity()
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}