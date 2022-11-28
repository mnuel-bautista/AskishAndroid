package dev.manuel.proyectomoviles.ui.fragments.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.manuel.proyectomoviles.R
import dev.manuel.proyectomoviles.models.PreguntasModel


class AdaptadorPreguntas(val preguntasList : ArrayList<PreguntasModel>) : RecyclerView.Adapter<AdaptadorPreguntas.MyViewHolder> (){
    public class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val question : TextView = itemView.findViewById(R.id.tv_pregunta)
        val answer : TextView = itemView.findViewById(R.id.tv_respu)
        val supportingText : TextView = itemView.findViewById(R.id.tv_descripcion)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdaptadorPreguntas.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_preguntas,parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdaptadorPreguntas.MyViewHolder, position: Int) {
        val preguntasModel : PreguntasModel = preguntasList[position]
        holder.question.text = preguntasModel.question
        holder.answer.text = preguntasModel.correctAnswer
        holder.supportingText.text = preguntasModel.supportingText
    }

    override fun getItemCount() = preguntasList.size
}