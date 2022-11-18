package dev.manuel.proyectomoviles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dev.manuel.proyectomoviles.databinding.FragmentPreguntasBinding


class FragmentPreguntas : Fragment() {

    private val firestore = Firebase.firestore

    private lateinit var binding: FragmentPreguntasBinding

    private lateinit var listenerRegistration: ListenerRegistration

    private var sala: DocumentSnapshot? = null

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_preguntas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPreguntasBinding.bind(view)
        auth = Firebase.auth

        val salaId = arguments?.getString("sala")

        if(salaId != null) {
            listenerRegistration = firestore.collection("salas")
                .document(salaId)
                .addSnapshotListener { value, _ ->
                    sala = value
                    val pregunta = value?.getString("pregunta")!!
                    val cuestionario = value.getDocumentReference("cuestionario")
                    val estado = value.getString("estado")

                    binding.pregunta.text = pregunta
                    cuestionario?.get()?.addOnSuccessListener {
                        (context as AppCompatActivity).supportActionBar!!.title = it.getString("cuestionario")
                    }

                    if(estado == "Completa") {
                        with(binding) {
                            opcionA.visibility = View.INVISIBLE
                            opcionB.visibility = View.INVISIBLE
                            opcionC.visibility = View.INVISIBLE
                            opcionD.visibility = View.INVISIBLE
                            esperandoResto.visibility = View.INVISIBLE
                            descripcion.visibility = View.VISIBLE
                            respuestaCorrecta.visibility = View.VISIBLE

                            val respuestaCorrectaId = value.getString("respuesta_correcta") ?: ""
                            val mRespuestaCorrecta = value.getString("respuestas.${respuestaCorrectaId}")
                            respuestaCorrecta.text = mRespuestaCorrecta
                            descripcion.text = value.getString("descripcion") ?: ""
                        }
                    }

                    val estadoSala = value.getString("estado_sala")

                    if(estadoSala == "Completada") {
                        findNavController().navigate(R.id.action_fragmentPreguntas_to_fragmentCuestionarioCompletado)
                    }
                }
        }

        binding.opcionA.setOnClickListener { chooseAnswer("A") }
        binding.opcionB.setOnClickListener { chooseAnswer("B") }
        binding.opcionC.setOnClickListener { chooseAnswer("C") }
        binding.opcionD.setOnClickListener { chooseAnswer("D") }
    }

    private fun chooseAnswer(answer: String) {
        val salaId = arguments?.getString("sala")
        val userId = arguments?.getString("userId")



        if(salaId != null) {
            val preguntaId = sala?.getString("id_pregunta")
            firestore.collection("salas")
                .document(salaId)
                .collection("resultados")
                .document(preguntaId!!)
                .update("$answer.$userId", true)
                .addOnSuccessListener {
                    binding.opcionA.visibility = View.INVISIBLE
                    binding.opcionB.visibility = View.INVISIBLE
                    binding.opcionC.visibility = View.INVISIBLE
                    binding.opcionD.visibility = View.INVISIBLE
                    binding.esperandoResto.visibility = View.VISIBLE
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        listenerRegistration.remove()
    }
}