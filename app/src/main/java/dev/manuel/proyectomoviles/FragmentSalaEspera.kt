package dev.manuel.proyectomoviles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FragmentSalaEspera : Fragment() {

    private val firestore = Firebase.firestore

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

        firestore.collection("usuarios")
            .whereEqualTo("email", "garcia.manuel.bss@gmail.com")
            .get()
            .addOnSuccessListener(::onUserRetrieved)
    }

    private fun onUserRetrieved(snapshot: QuerySnapshot) {
        if(snapshot.isEmpty) return
        userId = snapshot.documents.first().id

        val sala = snapshot.first().getString("sala")
        //User is in an active room
        if(sala != null) {
            listenerRegistration = firestore.collection("salas")
                .document(sala)
                .addSnapshotListener { value, error ->
                    if(value?.getBoolean("esperando") == false) {
                        val args = bundleOf("sala" to sala, "userId" to userId)
                        findNavController().navigate(R.id.action_fragmentSalaEspera_to_fragmentPreguntas, args)
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listenerRegistration.remove()
    }

}