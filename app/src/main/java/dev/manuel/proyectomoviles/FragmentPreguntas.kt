package dev.manuel.proyectomoviles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dev.manuel.proyectomoviles.databinding.FragmentPreguntasBinding
import dev.manuel.proyectomoviles.models.CurrentQuestion
import dev.manuel.proyectomoviles.models.CurrentQuestionStatus
import dev.manuel.proyectomoviles.repositories.QuizRoomStatus
import dev.manuel.proyectomoviles.repositories.QuizzRoomRepository
import kotlinx.coroutines.launch


class FragmentPreguntas : Fragment() {


    private val quizRoomRepository: QuizzRoomRepository = QuizzRoomRepository()

    private lateinit var binding: FragmentPreguntasBinding

    private var questionId: String = ""

    private lateinit var currentQuestion: CurrentQuestion

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

        val salaId = arguments?.getString("salaId") ?: ""

        quizRoomRepository.getQuizRoom(salaId)

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                quizRoomRepository.currentQuestion.collect {
                    currentQuestion = it
                    with(binding) {
                        if (it.status == CurrentQuestionStatus.InProgress) {
                            opcionA.visibility = View.VISIBLE
                            opcionB.visibility = View.VISIBLE
                            opcionC.visibility = View.VISIBLE
                            opcionD.visibility = View.VISIBLE
                            pregunta.text = it.question
                            esperandoResto.visibility = View.INVISIBLE
                            respuestaCorrecta.visibility = View.INVISIBLE
                            descripcion.visibility = View.INVISIBLE
                        }

                        if (it.status == CurrentQuestionStatus.Completed) {
                            opcionA.visibility = View.INVISIBLE
                            opcionB.visibility = View.INVISIBLE
                            opcionC.visibility = View.INVISIBLE
                            opcionD.visibility = View.INVISIBLE
                            descripcion.visibility = View.VISIBLE
                            respuestaCorrecta.visibility = View.VISIBLE
                            respuestaCorrecta.text = it.correctAnswer
                            descripcion.text = it.description
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                quizRoomRepository.quizGroup.collect {
                    (context as AppCompatActivity).supportActionBar!!.title = it
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                quizRoomRepository.quizRoomStatus.collect {
                    if (it == QuizRoomStatus.Completed) {
                        findNavController().navigate(R.id.action_fragmentPreguntas_to_fragmentCuestionarioCompletado)
                    }
                }
            }
        }

        binding.opcionA.setOnClickListener { chooseAnswer("a") }
        binding.opcionB.setOnClickListener { chooseAnswer("b") }
        binding.opcionC.setOnClickListener { chooseAnswer("c") }
        binding.opcionD.setOnClickListener { chooseAnswer("d") }
    }

    private fun chooseAnswer(answer: String) {
        val salaId = arguments?.getString("salaId")
        val userId = arguments?.getString("userId")

        if (salaId != null && userId != null) {
            viewLifecycleOwner.lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    quizRoomRepository.addAnswer(currentQuestion.questionId, currentQuestion.question, userId, salaId, answer)
                    with(binding) {
                        opcionA.visibility = View.INVISIBLE
                        opcionB.visibility = View.INVISIBLE
                        opcionC.visibility = View.INVISIBLE
                        opcionD.visibility = View.INVISIBLE
                        esperandoResto.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}