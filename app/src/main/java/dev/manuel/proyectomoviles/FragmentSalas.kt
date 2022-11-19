package dev.manuel.proyectomoviles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dev.manuel.proyectomoviles.databinding.FragmentSalasBinding
import dev.manuel.proyectomoviles.db.AppDatabase
import dev.manuel.proyectomoviles.models.Sala
import dev.manuel.proyectomoviles.ui.fragments.adapters.CardSalaAdapter


class FragmentSalas : Fragment() {

    private lateinit var binding: FragmentSalasBinding

    private val db: AppDatabase? = AppDatabase.getDatabase()


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
        db?.firestore?.collection("salas")
            ?.whereEqualTo("invitados.jaYl9hlDSAHCTWzA2ez5YWc1VhrQ", true)
            ?.get()
            ?.addOnSuccessListener {
                val rooms = it.documents.map { e ->
                    val id = e.id
                    val cuestionario = e["cuestionario.nombre"] as String
                    val grupo = e["grupo.nombre"] as String
                    val participantes = (e["participantes"] as HashMap<String, Boolean>).count()

                    Sala(id, cuestionario, grupo, participantes)
                }

                adapter.submitList(rooms)
            }

    }
}