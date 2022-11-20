package dev.manuel.proyectomoviles.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dev.manuel.proyectomoviles.R
import dev.manuel.proyectomoviles.dataClass.Usuario
import dev.manuel.proyectomoviles.databinding.FragmentRegistroBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class FragmentRegistro : Fragment() {
    private lateinit var nombre: EditText
    private lateinit var username: EditText
    private lateinit var correo: EditText
    private lateinit var password: EditText
    private lateinit var auth:FirebaseAuth

    private lateinit var btnConfirmar: MaterialButton
    private lateinit var btnVolver: MaterialButton


    private var _binding: FragmentRegistroBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegistroBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        val db = Firebase.firestore

        nombre = view.findViewById(R.id.txtNombre)
        username = view.findViewById(R.id.txtUsername)
        password = view.findViewById(R.id.txtPass)
        correo = view.findViewById(R.id.txtCorreo)

        btnConfirmar = view.findViewById(R.id.btnConfirmar)
        btnVolver = view.findViewById(R.id.btnVolver)

        val usuario = Usuario(correo.toString(), nombre.toString(), username.toString())

        btnConfirmar.setOnClickListener {
            if (correo.toString().isNotEmpty() &&
                password.toString().isNotEmpty()
            ) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    correo.toString(), password.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        db.collection("usuarios").document(correo.toString()).set(usuario) //UID
                        findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
                    } else {
                        showAlert()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showAlert(): AlertDialog? {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Oh no... ¡Un error!")
            .setMessage("Ha ocurrido un error, intente de nuevo.")
            .setPositiveButton("Ok", null)
        return builder.create()
    }

}