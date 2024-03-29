package dev.manuel.proyectomoviles

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
import dev.manuel.proyectomoviles.viewmodels.QuizRoomViewModel
import kotlinx.coroutines.launch


class FragmentPreguntas : Fragment() {


    private val viewModel: QuizRoomViewModel by viewModels()

    private lateinit var binding: FragmentPreguntasBinding

    private lateinit var currentQuestion: CurrentQuestion

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        })
    }

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

        viewModel.repository.getQuizRoom(salaId)

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.repository.currentQuestion.collect {
                    currentQuestion = it
                    with(binding) {
                        if (it.status == CurrentQuestionStatus.InProgress) {
                            binding.root.transitionToState(R.id.next_question_end)
                            pregunta.text = it.question
                        }

                        if (it.status == CurrentQuestionStatus.Completed) {
                            binding.root.transitionToState(R.id.question_completed_end)
                            respuestaCorrecta.text = it.correctAnswer
                            descripcion.text = it.description
                        }

                        textA.text = it.answers["a"]
                        textB.text = it.answers["b"]
                        textC.text = it.answers["c"]
                        textD.text = it.answers["d"]
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.repository.quizGroup.collect {
                    (context as AppCompatActivity).supportActionBar!!.title = it
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.repository.quizRoomStatus.collect {
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
                    binding.root.transitionToState(R.id.action_buttons_end)
                    viewModel.repository.addAnswer(currentQuestion.questionId, currentQuestion.question, userId, salaId, answer)
                    viewModel.repository.addAnswerToUserCollection(
                        userId = userId,
                        quizId = viewModel.repository.quizId.value,
                        quizName = viewModel.repository.quizName.value,
                        answer = answer,
                        questionId = currentQuestion.questionId,
                        questionName = currentQuestion.question,
                        correctAnswer = currentQuestion.correctAnswer,
                        supportingText = currentQuestion.description
                    )
                }
            }
        }
    }
}