package dev.manuel.proyectomoviles.ui.fragments.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.manuel.proyectomoviles.R

class GruposAdapter : RecyclerView.Adapter<GruposAdapter.NombreViewHolder>() {

    private var listNombres = listOf<String>()

    class NombreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val vistaNombre: TextView = itemView.findViewById(R.id.tvNombre)
    }

    fun setListNames(list: List<String>) {
        listNombres = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NombreViewHolder {
        return NombreViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NombreViewHolder, position: Int) {
        holder.vistaNombre.text = listNombres[position]
    }

    override fun getItemCount(): Int = listNombres.size
}