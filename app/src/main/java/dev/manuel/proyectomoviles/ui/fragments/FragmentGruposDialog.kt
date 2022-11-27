package dev.manuel.proyectomoviles.ui.fragments

import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.AllCaps
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import dev.manuel.proyectomoviles.R
import dev.manuel.proyectomoviles.databinding.FragmentGruposDialogBinding
import dev.manuel.proyectomoviles.db.AppDatabase


class FragmentGruposDialog : DialogFragment() {

    private lateinit var binding: FragmentGruposDialogBinding
    private val firestore = AppDatabase.getDatabase()?.firestore

    private lateinit var entrada: TextInputEditText
    private lateinit var unirse: Button
    private lateinit var cancelar: Button

    //Modificar para hacerlo dinamico, solo de prueba - 18VC
    //private val idUsuario = (requireActivity() as MainActivity).getUserId()
    private val idUsuario = "hcBYmE4It2lsjb1KYD9J"
    private lateinit var code: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_grupos_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGruposDialogBinding.bind(view)

        entrada = binding.tiEntrada
        unirse = binding.btnUnirse
        cancelar = binding.btnCancelar

        entrada.filters = arrayOf<InputFilter>(AllCaps())

        unirse.setOnClickListener {
            code = entrada.text.toString()
            registrarUsuario()
        }

        cancelar.setOnClickListener {
            dismiss()
        }
    }

    private fun registrarUsuario() {
        firestore?.collection("groups")
            ?.whereEqualTo("code", code)
            ?.get()
            ?.addOnSuccessListener {
                if (it.documents.firstOrNull() != null) {
                    it.documents.first().reference.update(mapOf("members.$idUsuario" to true))
                    //Cambiar el requireView()
                    parentFragment?.let { it1 ->
                        Snackbar.make(
                            it1.requireView(),
                            "¡Bienvenido!",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    dismiss()
                } else {
                    parentFragment?.let { it1 ->
                        Snackbar.make(
                            it1.requireView(),
                            "No se ha encontrado el grupo",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
    }
}