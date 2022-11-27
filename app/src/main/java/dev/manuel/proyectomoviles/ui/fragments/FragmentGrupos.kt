package dev.manuel.proyectomoviles.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.manuel.proyectomoviles.R
import dev.manuel.proyectomoviles.adapters.GrupoAdapter
import dev.manuel.proyectomoviles.databinding.FragmentGruposBinding
import dev.manuel.proyectomoviles.db.AppDatabase
import dev.manuel.proyectomoviles.getUserId


class FragmentGrupos : Fragment() {

    private lateinit var recycleView: RecyclerView
    private val firestore = AppDatabase.getDatabase()?.firestore

    private lateinit var binding: FragmentGruposBinding

    private val groups = ArrayList<String>()

    private val idUsuario = "hcBYmE4It2lsjb1KYD9J"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val userId = requireActivity().getUserId()
        if(userId == "") {
            findNavController().navigate(R.id.action_fragmentGrupos_to_FragmentLogin)
        }
        return inflater.inflate(R.layout.fragment_grupos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGruposBinding.bind(view)

        recycleView = binding.recycleView
        recycleView.layoutManager = LinearLayoutManager(requireContext())
        recycleView.adapter = GrupoAdapter()
        leerGrupos()
    }

    private fun leerGrupos() {
        //Mostrar los grupos a los que pertenece el usuario
        firestore?.collection("groups")
            ?.whereEqualTo("members.$idUsuario", true)
            ?.addSnapshotListener { value, _ ->
                val groups = value?.documents?.map { it.getString("group") ?: "" }
                if (groups?.isNotEmpty() == true) {
                    (recycleView.adapter as GrupoAdapter).setListNames(groups)
                }
            }
    }
}