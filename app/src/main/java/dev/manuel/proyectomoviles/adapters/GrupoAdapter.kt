package dev.manuel.proyectomoviles.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.manuel.proyectomoviles.R

class GrupoAdapter: RecyclerView.Adapter<GrupoAdapter.NombreViewHolder>() {

    private var listNombres = ArrayList<String>()

    class NombreViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val vistaNombre: TextView = itemView.findViewById(R.id.tvNombre)
    }

    fun setListNames(list: ArrayList<String>){
        println("Array adaptador $list")
        listNombres = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NombreViewHolder {
        return NombreViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))
    }

    override fun onBindViewHolder(holder: NombreViewHolder, position: Int) {
        holder.vistaNombre.text = listNombres[position]
    }

    override fun getItemCount(): Int {
        return listNombres.size
    }
}