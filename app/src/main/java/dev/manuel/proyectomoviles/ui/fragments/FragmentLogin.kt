package dev.manuel.proyectomoviles.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.findFragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dev.manuel.proyectomoviles.R
import dev.manuel.proyectomoviles.dataClass.Usuario
import dev.manuel.proyectomoviles.databinding.FragmentLoginBinding
import dev.manuel.proyectomoviles.databinding.FragmentRegistroBinding


class FragmentLogin : Fragment() {
    private lateinit var correo: EditText
    private lateinit var password: EditText
    private lateinit var btnIngresar: MaterialButton
    private lateinit var btnRegistrar:MaterialButton
    private lateinit var btnGoogle:MaterialButton
    private lateinit var btnNumero:MaterialButton
    private lateinit var usuario: Usuario
    private lateinit var auth: FirebaseAuth

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        val db = Firebase.firestore

        correo = view.findViewById(R.id.txtCorreo)
        password = view.findViewById(R.id.txtPass)

        btnRegistrar = view.findViewById(R.id.btnReg)
        btnIngresar = view.findViewById(R.id.btnIngresar)

        btnGoogle = view.findViewById(R.id.btnGoogle)
        btnNumero = view.findViewById(R.id.btnTelefono)

        btnRegistrar.setOnClickListener{
            view -> view.findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        btnIngresar.setOnClickListener {
            if (correo.toString().isNotEmpty() && password.toString().isNotEmpty()) {
                auth.signInWithEmailAndPassword(correo.toString(), password.toString()).addOnCompleteListener{
                    if(it.isSuccessful){
                        db.collection("usuarios").whereEqualTo("correo", correo.toString())
                            .get().addOnSuccessListener { documents ->

                                for (document in documents){
                                    usuario = Usuario(
                                        "${document.data["correo"]}",
                                        "${document.data["nombre"]}",
                                        "${document.data["username"]}"
                                    )
                                }
                                findNavController().navigate(R.id.fragmentGrupos)
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

