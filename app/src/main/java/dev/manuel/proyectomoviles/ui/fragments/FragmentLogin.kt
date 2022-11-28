package dev.manuel.proyectomoviles.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import dev.manuel.proyectomoviles.*
import dev.manuel.proyectomoviles.dataClass.Usuario
import dev.manuel.proyectomoviles.databinding.FragmentLoginBinding
import dev.manuel.proyectomoviles.db.AppDatabase


class FragmentLogin : Fragment() {

    private val GOOGLE_SIGN_IN = 100
    private lateinit var usuario: Usuario
    private val firestore = AppDatabase.getDatabase()?.firestore
    private val auth = AppDatabase.getDatabase()?.auth

    private var _binding: FragmentLoginBinding? = null


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
    ): View? {

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

        binding.btnGoogle.setOnClickListener{
            //Autenticacion de google
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleClient:GoogleSignInClient = GoogleSignIn.getClient(requireActivity(), googleConf)
            googleClient.signOut()

            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val preferences = requireActivity().getSharedPreferences(UserCredentialsPreferences, Context.MODE_PRIVATE)

        if (requestCode == GOOGLE_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)

            try{
            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)

            if (account != null) {
                val credential: AuthCredential =
                    GoogleAuthProvider.getCredential(account.idToken, null)

                auth?.signInWithCredential(credential)?.addOnCompleteListener { res ->
                    if (res.isSuccessful) {
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

            } catch (e: ApiException){
                showAlert()
            }
        }
        }
    }

