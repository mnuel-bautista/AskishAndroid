package dev.manuel.proyectomoviles.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dev.manuel.proyectomoviles.MainActivity
import dev.manuel.proyectomoviles.R
import dev.manuel.proyectomoviles.databinding.FragmentRegistroBinding
import dev.manuel.proyectomoviles.databinding.FragmentTelefonoBinding
import dev.manuel.proyectomoviles.db.AppDatabase
import dev.manuel.proyectomoviles.setUserId

class FragmentTelefono : Fragment() {

    private val firestore = AppDatabase.getDatabase()?.firestore
    private val auth = AppDatabase.getDatabase()?.auth

    private var _binding: FragmentTelefonoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_telefono, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val name = binding.txtNombre.editText?.editableText
        val username = binding.txtUsername.editText?.editableText
        val password = binding.VerificationCode.editText?.editableText
        val email = binding.phoneEditText.editText?.editableText

        val btnVerificar = binding.btnVerificar
        val btnVolver = binding.btnVolver
        val btnMandar = binding.btnMandar

        btnVolver.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentTelefono_to_fragmentLogin)
        }

        btnMandar.setOnClickListener {

        }

        btnVerificar.setOnClickListener {
            if (email.toString().isNotEmpty() &&
                password.toString().isNotEmpty()
            ) {

                auth?.createUserWithEmailAndPassword(
                    email.toString(), password.toString()
                )?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        val userId = it.result.user!!.uid
                        firestore?.collection("users")?.document(it.result.user!!.uid)
                            ?.set(mapOf("name" to name.toString(), "username" to username.toString(), "email" to email.toString()))
                            ?.addOnSuccessListener {
                                (requireActivity() as MainActivity).setUserId(userId)
                                findNavController().navigate(R.id.action_FragmentTelefono_to_fragmentGrupos)
                            }
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