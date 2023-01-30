package com.example.firebase01_23_01_23

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebase01_23_01_23.adapters.UsuariosAdapter
import com.example.firebase01_23_01_23.databinding.ActivityHomeBinding
import com.example.firebase01_23_01_23.models.Usuarios
import com.example.firebase01_23_01_23.prefs.Prefs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    private lateinit var prefs: Prefs
    private var listaUsuarios = ArrayList<Usuarios>()
    private lateinit var adapter: UsuariosAdapter
    private var db = Firebase.database
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = FirebaseDatabase.getInstance("https://fir-2023-78680-default-rtdb.europe-west1.firebasedatabase.app/")
        prefs = Prefs(this)
        setListeners()
        setRecycler()
        traerUsuarios()

    }
//-------------------------------------------------------------------------------------------------
    private fun traerUsuarios() {
        db.getReference("usuarios").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaUsuarios.clear()
                if (snapshot.exists()) {
                    for (item in snapshot.children) {
                        val usuario = item.getValue(Usuarios::class.java)
                        if (usuario != null) {
                            listaUsuarios.add(usuario)
                        }
                    }
                    adapter.lista = listaUsuarios
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    //-------------------------------------------------------------------------------------------------
    private fun setRecycler() {
        var layoutManager = LinearLayoutManager(this)
        binding.recUsers.layoutManager = layoutManager
        adapter = UsuariosAdapter(listaUsuarios, { onItemBorrar(it) }) { onItemEditar(it) }
        binding.recUsers.adapter = adapter

    }

    private fun onItemEditar(usuario: Usuarios) {
        val intent = Intent(this, CrearActivity::class.java).apply {
            putExtra("usuario", usuario)
        }
        startActivity(intent)
    }

    private fun onItemBorrar(position: Int) {
        //Apuntamos al nodo que queremos borrar
        db.getReference("usuarios").child(listaUsuarios[position].userName.toString()).removeValue()
        listaUsuarios.removeAt(position)
        adapter.notifyItemRemoved(position)
        adapter.notifyItemRangeChanged(position, listaUsuarios.size)

    }

    //-------------------------------------------------------------------------------------------------
    private fun setListeners() {
        binding.btnAdd.setOnClickListener {
            startActivity(Intent(this, CrearActivity::class.java))
        }
    }
//-------------------------------------------------------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_option, menu)
        return super.onCreateOptionsMenu(menu)
    }
//-------------------------------------------------------------------------------------------------
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