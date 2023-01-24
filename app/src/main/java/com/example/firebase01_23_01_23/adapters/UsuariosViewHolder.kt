package com.example.firebase01_23_01_23.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.firebase01_23_01_23.R
import com.example.firebase01_23_01_23.databinding.UsersLayoutBinding
import com.example.firebase01_23_01_23.models.Usuarios

class UsuariosViewHolder(v: View): RecyclerView.ViewHolder(v) {
    private val binding = UsersLayoutBinding.bind(v)

    fun render(usuarios: Usuarios) {
        binding.tvNombre.text = usuarios.nombre
        binding.tvApellidos.text = usuarios.apellidos
        binding.tvEdad.text = String.format(binding.tvEdad.context.getString(R.string.edad), usuarios.edad)
        binding.tvUserName.text = usuarios.userName
    }

}
