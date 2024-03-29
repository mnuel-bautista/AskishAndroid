package dev.manuel.proyectomoviles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dev.manuel.proyectomoviles.databinding.FragmentSalasBinding
import dev.manuel.proyectomoviles.ui.fragments.adapters.CardSalaAdapter
import dev.manuel.proyectomoviles.viewmodels.QuizRoomViewModel
import kotlinx.coroutines.launch


class FragmentSalas : Fragment() {

    private lateinit var binding: FragmentSalasBinding

    private val viewModel: QuizRoomViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_salas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSalasBinding.bind(view)

        val adapter = CardSalaAdapter {
            val args = bundleOf("salaId" to it.id)
            findNavController().navigate(R.id.action_fragmentSalas_to_fragmentSalaEspera, args)
        }
        binding.salasRecyclerView.adapter = adapter
        binding.salasRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        getRooms(adapter)
    }


    private fun getRooms(adapter: CardSalaAdapter) {
        val userId = (requireActivity() as MainActivity).getUserId()
        viewModel.repository.getAllQuizRooms(userId)
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.repository.quizRooms.collect {
                    adapter.submitList(it)
                }
            }
        }
    }
}