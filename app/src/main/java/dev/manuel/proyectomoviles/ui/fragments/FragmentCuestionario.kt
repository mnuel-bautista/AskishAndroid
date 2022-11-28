package dev.manuel.proyectomoviles.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import dev.manuel.proyectomoviles.MainActivity
import dev.manuel.proyectomoviles.R
import dev.manuel.proyectomoviles.db.AppDatabase
import dev.manuel.proyectomoviles.getUserId
import dev.manuel.proyectomoviles.models.PreguntasModel
import dev.manuel.proyectomoviles.ui.fragments.adapters.AdaptadorPreguntas


class FragmentCuestionario : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var preguntasArrayList: ArrayList<PreguntasModel>
    private lateinit var adaptadorPreguntas: AdaptadorPreguntas
    private val firestore = AppDatabase.getDatabase()?.firestore
    private var quizId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_cuestionario, container, false)
        recyclerView = root.findViewById(R.id.listaPreguntas)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        quizId = arguments?.getString("quizId") ?: ""
        val quizName = arguments?.getString("quiz") ?: ""

        (requireActivity() as MainActivity).supportActionBar?.title = quizName

        preguntasArrayList = arrayListOf()

        adaptadorPreguntas = AdaptadorPreguntas(preguntasArrayList)

        recyclerView.adapter = adaptadorPreguntas

        EventChangeListener()
        return root
    }


    private fun EventChangeListener() {
        val userId = requireActivity().getUserId()
        firestore?.collection("users/${userId}/quizzes/$quizId/questions")
            ?.addSnapshotListener(object : com.google.firebase.firestore.EventListener<QuerySnapshot>{
            override fun onEvent(
                value: QuerySnapshot?,
                error: FirebaseFirestoreException?
            ) {
                if (error != null){
                    Log.e("Firestore Error", error.message.toString())
                    return
                }
                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        preguntasArrayList.add(dc.document.toObject(PreguntasModel::class.java))
                    }
                }
                adaptadorPreguntas.notifyDataSetChanged()
            }

        })
    }
}