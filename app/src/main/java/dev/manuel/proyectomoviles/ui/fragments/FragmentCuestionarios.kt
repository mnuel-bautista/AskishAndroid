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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import dev.manuel.proyectomoviles.R
import dev.manuel.proyectomoviles.db.AppDatabase
import dev.manuel.proyectomoviles.getUserId
import dev.manuel.proyectomoviles.models.CuestionariosModel
import dev.manuel.proyectomoviles.ui.fragments.adapters.AdaptadorCuestionarios


class FragmentCuestionarios : Fragment(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var cuestionariosArrayList: ArrayList<CuestionariosModel>
    private lateinit var adaptadorCuestionarios: AdaptadorCuestionarios
    private val firestore = AppDatabase.getDatabase()?.firestore
    private lateinit var obtenerId : String
    private val idUsuario = "okGsS2gwYYzuUOIOCOOH"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_cuestionarios, container, false)
        recyclerView = root.findViewById(R.id.list_Cuestionarios)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        cuestionariosArrayList = arrayListOf()

        adaptadorCuestionarios = AdaptadorCuestionarios(cuestionariosArrayList)

        recyclerView.adapter = adaptadorCuestionarios

        EventChangeListener()


        adaptadorCuestionarios.setOnItemClickListener(object : AdaptadorCuestionarios.onItemClickListener{

            override fun onItemClick(position: Int) {

                if (position == 0) findNavController().navigate(R.id.fragmentCuestionario) //Moverse entre pantallas
            }
        })




        return root
    }

    fun getAllDocumentsUsuarios (){
        var id : String
        firestore?.collection("Quiz")?.get()?.addOnSuccessListener { resultado ->
            for (documentos in resultado){
                id = documentos.id
                println(documentos.id)
            }
        }
    }


    private fun EventChangeListener() {
        firestore?.collection("Quiz")?.
            whereEqualTo("Quiz.${idUsuario}", true)?.
                addSnapshotListener(object : com.google.firebase.firestore.EventListener<QuerySnapshot>{
                    override fun onEvent(
                        value: QuerySnapshot?,
                        error: FirebaseFirestoreException?
                    ) {
                        if (error != null){
                            Log.e("Firestore Error", error.message.toString())
                            return
                        }
                        for (dc : DocumentChange in value?.documentChanges!!){
                            if (dc.type == DocumentChange.Type.ADDED){
                                cuestionariosArrayList.add(dc.document.toObject(CuestionariosModel::class.java))
                            }
                        }
                        adaptadorCuestionarios.notifyDataSetChanged()
                    }

                })
    }

}