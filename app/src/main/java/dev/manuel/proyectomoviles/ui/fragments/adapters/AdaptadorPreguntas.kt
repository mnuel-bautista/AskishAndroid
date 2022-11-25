package dev.manuel.proyectomoviles.ui.fragments.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.manuel.proyectomoviles.R
import dev.manuel.proyectomoviles.models.PreguntasModel
import io.grpc.Context

class AdaptadorPreguntas : RecyclerView.Adapter<AdaptadorPreguntas.ViewHolder>(){

    var pregunta : MutableList<PreguntasModel> = ArrayList()
    lateinit var context : android.content.Context

    //Constructor
    fun AdaptadorPreguntas(pregunta: MutableList<PreguntasModel>, context: android.content.Context){
        this.pregunta = pregunta
        this.context = context
    }

    class ViewHolder (view : View): RecyclerView.ViewHolder(view){
        val pregunta:TextView
        val respu_correcta:TextView
        val descripcion:TextView

        init {
            pregunta = view.findViewById(R.id.tv_pregunta)
            respu_correcta = view.findViewById(R.id.tv_respu_correcta)
            descripcion = view.findViewById(R.id.tv_descripcion)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_preguntas, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.pregunta.text = pregunta[position].pregunta
        holder.respu_correcta.text = pregunta[position].respu_correcta
        holder.descripcion.text = pregunta[position].descripcion
    }

    override fun getItemCount() = pregunta.size
}