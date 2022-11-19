package dev.manuel.proyectomoviles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.*
import dev.manuel.proyectomoviles.db.AppDatabase

class FragmentSalaEspera : Fragment() {

    private val database: AppDatabase? = AppDatabase.getDatabase()

    private lateinit var listenerRegistration: ListenerRegistration

    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sala_espera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onUserRetrieved()
    }

    private fun onUserRetrieved() {
        val salaArg = arguments?.getString("salaId") ?: ""
        listenerRegistration = database?.firestore?.document("salas/$salaArg")
            ?.addSnapshotListener { value, _ ->
                if(value != null) {
                    if(value.getString("estado_sala") == "In Progress") {
                        val args = bundleOf("salaId" to value.id, "userId" to "jaYl9hlDSAHCTWzA2ez5YWc1VhrQ")
                        findNavController().navigate(R.id.action_fragmentSalaEspera_to_fragmentPreguntas, args)
                    }
                }
            }!!
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listenerRegistration.remove()
    }

}