package dev.manuel.proyectomoviles.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.manuel.proyectomoviles.R
import dev.manuel.proyectomoviles.UserCredentialsPreferences
import dev.manuel.proyectomoviles.dataClass.Usuario
import dev.manuel.proyectomoviles.databinding.FragmentLoginBinding
import dev.manuel.proyectomoviles.db.AppDatabase
import dev.manuel.proyectomoviles.getUserId


class FragmentLogin : Fragment() {

    private lateinit var usuario: Usuario
    private val firestore = AppDatabase.getDatabase()?.firestore
    private val auth = AppDatabase.getDatabase()?.auth

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val userId = requireActivity().getUserId()
        if(userId != "") {
            findNavController().navigate(R.id.action_FragmentLogin_to_fragmentGrupos)
        }

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val preferences = requireActivity().getSharedPreferences(UserCredentialsPreferences, Context.MODE_PRIVATE)
        binding.btnReg.setOnClickListener{
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        binding.btnIngresar.setOnClickListener {
            val emailField = binding.userEmailEditText.editText?.editableText?.toString()
            val passwordField = binding.userPasswordText.editableText.toString()

            if (emailField?.isNotEmpty() == true && passwordField.isNotEmpty()) {
                auth?.signInWithEmailAndPassword(emailField, passwordField)
                    ?.addOnCompleteListener{ res ->
                        if(res.isSuccessful){
                            preferences.edit(commit = true) {
                                putString("userId", res.result.user?.uid)
                            }
                            findNavController().navigate(R.id.action_FragmentLogin_to_fragmentGrupos)
                        } else {
                            showAlert()
                        }
                    }?.addOnFailureListener {
                        it
                    }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showAlert() {

        val builder = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Oh no... ¡Un error!")
            .setMessage("Ha ocurrido un error, intente de nuevo.")
            .setPositiveButton("Ok", null)
        builder.show()
    }
}

