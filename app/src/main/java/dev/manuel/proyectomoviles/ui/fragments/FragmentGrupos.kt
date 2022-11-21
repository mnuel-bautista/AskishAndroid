package dev.manuel.proyectomoviles.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dev.manuel.proyectomoviles.R
import dev.manuel.proyectomoviles.adapters.GrupoAdapter
import dev.manuel.proyectomoviles.databinding.FragmentGruposBinding
import dev.manuel.proyectomoviles.db.AppDatabase


class FragmentGrupos : Fragment() {

    private lateinit var recycleView: RecyclerView
    private val firestore = AppDatabase.getDatabase()?.firestore

    private lateinit var binding: FragmentGruposBinding

    val idUsuario = "hcBYmE4It2lsjb1KYD9J"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        leerGrupos()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_grupos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGruposBinding.bind(view)

        recycleView = binding.recycleView
        recycleView.layoutManager = LinearLayoutManager(requireContext())
        recycleView.adapter = GrupoAdapter()
    }

    private fun leerGrupos() {
        //Mostrar los grupos a los que pertenece el usuario
//        db.collection("grupos").whereEqualTo("codigo", "1122")
//            .get()
//            .addOnSuccessListener {
//                it.documents.first().reference.update(mapOf("integrantes.asdfasdfasd" to true))
//            }

        val nombre = ArrayList<String>()

        firestore?.collection("grupos")
            ?.whereEqualTo("integrantes.$idUsuario", true)
            ?.addSnapshotListener{ value, e ->
                for (doc in value!!){
                    doc.getString("nombre")?.let {
                        nombre.add(it)
                    }
                }
                if (nombre.isNotEmpty()){
                    (recycleView.adapter as GrupoAdapter).setListNames(nombre)
                }
                println("Array $nombre")
        }
    }
}