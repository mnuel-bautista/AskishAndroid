package dev.manuel.proyectomoviles.ui.fragments.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import dev.manuel.proyectomoviles.R
import dev.manuel.proyectomoviles.models.Sala


class CardSalaAdapter(val onCardClick: (Sala) -> Unit = {}) :
    androidx.recyclerview.widget.ListAdapter<Sala, CardSalaAdapter.SalaViewHolder>(
        diffCallback
    ) {


    class SalaViewHolder(itemView: View) : ViewHolder(itemView) {
        val cuestionario: TextView = itemView.findViewById(R.id.card_sala_cuestionario)
        val grupo: TextView = itemView.findViewById(R.id.card_sala_grupo)
        val participantes: TextView = itemView.findViewById(R.id.card_sala_participantes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_sala, parent, false)
        return SalaViewHolder(view)
    }

    override fun onBindViewHolder(holder: SalaViewHolder, position: Int) {
        holder.cuestionario.text = getItem(position).cuestionario
        holder.grupo.text = getItem(position).grupo
        holder.participantes.text = getItem(position).participantes.toString()
        holder.itemView.setOnClickListener { onCardClick(getItem(position)) }
    }

}


val diffCallback = object : DiffUtil.ItemCallback<Sala>() {
    override fun areItemsTheSame(oldItem: Sala, newItem: Sala): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Sala, newItem: Sala): Boolean {
        return oldItem == newItem
    }
}