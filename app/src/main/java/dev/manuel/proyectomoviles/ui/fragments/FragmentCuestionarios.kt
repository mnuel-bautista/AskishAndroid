package dev.manuel.proyectomoviles.ui.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import dev.manuel.proyectomoviles.MainActivity
import dev.manuel.proyectomoviles.R
import dev.manuel.proyectomoviles.db.AppDatabase
import dev.manuel.proyectomoviles.getUserId
import dev.manuel.proyectomoviles.models.CuestionariosModel
import dev.manuel.proyectomoviles.setUserId
import dev.manuel.proyectomoviles.ui.fragments.adapters.AdaptadorCuestionarios


class FragmentCuestionarios : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var cuestionariosArrayList: ArrayList<CuestionariosModel>
    private lateinit var adaptadorCuestionarios: AdaptadorCuestionarios
    private val firestore = AppDatabase.getDatabase()?.firestore
    private var idQuestions : String = "Gae6sz10OEvLf2hKUOCL" //Ayuda
    private var idUsuario: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        idUsuario = requireActivity().getUserId()
        val root = inflater.inflate(R.layout.fragment_cuestionarios, container, false)
        recyclerView = root.findViewById(R.id.list_Cuestionarios)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        cuestionariosArrayList = arrayListOf()

        adaptadorCuestionarios = AdaptadorCuestionarios(cuestionariosArrayList)

        recyclerView.adapter = adaptadorCuestionarios

        EventChangeListener()


        adaptadorCuestionarios.setOnItemClickListener(object :
            AdaptadorCuestionarios.onItemClickListener {

            override fun onItemClick(position: Int) {
                val bundle = bundleOf("idQuestions" to idQuestions)
               findNavController().navigate(R.id.fragmentCuestionario, bundle) //Moverse entre pantallas
            }
        })

        return root
    }

    private fun EventChangeListener() {
        firestore?.collection("users/${idUsuario}/quizzes")
            ?.addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Firestore Error", error.message.toString())
                    return@addSnapshotListener
                }
                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        cuestionariosArrayList.add(dc.document.toObject(CuestionariosModel::class.java))
                    }
                }
                adaptadorCuestionarios.notifyDataSetChanged()
            }
    }

}