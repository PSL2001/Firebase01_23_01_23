package com.example.firebase01_23_01_23.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebase01_23_01_23.R
import com.example.firebase01_23_01_23.models.Usuarios

class UsuariosAdapter(var lista: List<Usuarios>): RecyclerView.Adapter<UsuariosViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuariosViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.users_layout, parent, false)
        return UsuariosViewHolder(v)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: UsuariosViewHolder, position: Int) {
        holder.render(lista[position])
    }
}