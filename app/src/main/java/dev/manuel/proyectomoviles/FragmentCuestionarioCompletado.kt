package dev.manuel.proyectomoviles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dev.manuel.proyectomoviles.databinding.FragmentCuestionarioCompletadoBinding


class FragmentCuestionarioCompletado : Fragment() {


    private lateinit var binding: FragmentCuestionarioCompletadoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cuestionario_completado, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCuestionarioCompletadoBinding.bind(view)

        binding.salir.setOnClickListener {
            findNavController().popBackStack(R.id.fragmentSalas, false)
        }
    }

}