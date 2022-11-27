package dev.manuel.proyectomoviles

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.*
import dev.manuel.proyectomoviles.db.AppDatabase

class FragmentSalaEspera : Fragment() {

    private val database: AppDatabase? = AppDatabase.getDatabase()

    private lateinit var listenerRegistration: ListenerRegistration

    private var status = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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
        val userId = (requireActivity() as MainActivity).getUserId()
        listenerRegistration = database?.firestore?.document("salas/$salaArg")
            ?.addSnapshotListener { value, _ ->

                if(status == value?.getString("quizRoomStatus")) {
                    return@addSnapshotListener
                }

                if(value != null) {
                    if(value.getString("quizRoomStatus") == "In Progress") {
                        val args = bundleOf("salaId" to value.id, "userId" to userId)
                        status = value.getString("quizRoomStatus") ?: ""
                        findNavController().navigate(R.id.action_fragmentSalaEspera_to_fragmentPreguntas, args)
                    }
                }
            }!!
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listenerRegistration.remove()
    }

}