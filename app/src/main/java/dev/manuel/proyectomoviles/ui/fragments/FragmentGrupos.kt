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


class FragmentGrupos : Fragment() {

    private lateinit var recycleView: RecyclerView
    private val db = Firebase.firestore
    private val nombre = ArrayList<String>()

    private lateinit var binding: FragmentGruposBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_grupos, container, false)
    }

    //CODIGO AGREGADO
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGruposBinding.bind(view)

        //Referenciar al layout del recycleview
        recycleView = binding.recycleView
        recycleView.layoutManager = LinearLayoutManager(requireContext())
        recycleView.adapter = GrupoAdapter()

        leer()
    }

    private fun leer() {

        //Mostrar los grupos a los que pertenece el usuario
//        db.collection("grupos").whereEqualTo("codigo", "1122")
//            .get()
//            .addOnSuccessListener {
//                it.documents.first().reference.update(mapOf("integrantes.asdfasdfasd" to true))
//            }

//        db.collection("grupos").whereEqualTo("integrantes.aSDFASDFasdfasd", true).addSnapshotListener{ value, e ->
//            for (doc in value!!){
//                doc.getString("nombre")?.let {
//                    nombre.add(it)
//                }
//            }
//            if (nombre.isNotEmpty()){
//                (recycleView.adapter as GrupoAdapter).setListNames(nombre)
//            }
//            println("Array $nombre")
//        }
    }

}