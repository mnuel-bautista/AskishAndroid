package dev.manuel.proyectomoviles.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dev.manuel.proyectomoviles.MainActivity
import dev.manuel.proyectomoviles.R
import dev.manuel.proyectomoviles.databinding.FragmentRegistroBinding
import dev.manuel.proyectomoviles.db.AppDatabase
import dev.manuel.proyectomoviles.removeMenu
import dev.manuel.proyectomoviles.setUserId

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class FragmentRegistro : Fragment() {

    private val firestore = AppDatabase.getDatabase()?.firestore
    private val auth = AppDatabase.getDatabase()?.auth



    private var _binding: FragmentRegistroBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as MainActivity).removeMenu()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegistroBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val name = binding.txtNombre.editText?.editableText
        val username = binding.txtUsername.editText?.editableText
        val password = binding.txtPass.editText?.editableText
        val email = binding.emailEditText.editText?.editableText

        val btnConfirmar = binding.btnConfirmar
        val btnVolver = binding.btnVolver

        btnVolver.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        btnConfirmar.setOnClickListener {
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
                                findNavController().navigate(R.id.action_FragmentRegistro_to_fragmentGrupos)
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