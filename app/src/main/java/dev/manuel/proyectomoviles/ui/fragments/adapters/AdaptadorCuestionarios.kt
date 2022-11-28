package dev.manuel.proyectomoviles.ui.fragments.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.manuel.proyectomoviles.R
import dev.manuel.proyectomoviles.models.CuestionariosModel


class AdaptadorCuestionarios(val cuestionariosList : ArrayList<CuestionariosModel>) : RecyclerView.Adapter<AdaptadorCuestionarios.MyViewHolder> (){
    public class MyViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){

        val quiz : TextView = itemView.findViewById(R.id.tv_cuestionarios)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    private lateinit var mListener : onItemClickListener
    interface onItemClickListener {
        fun onItemClick(Position : Int)
    }

    fun setOnItemClickListener(listener:onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdaptadorCuestionarios.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_cuestionarios,parent, false)

        return MyViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: AdaptadorCuestionarios.MyViewHolder, position: Int) {
        val cuestionariosModel : CuestionariosModel = cuestionariosList[position]
        holder.quiz.text = cuestionariosModel.quiz
    }

    override fun getItemCount() = cuestionariosList.size
}