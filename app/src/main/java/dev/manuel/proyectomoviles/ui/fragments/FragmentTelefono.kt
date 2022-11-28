package dev.manuel.proyectomoviles.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dev.manuel.proyectomoviles.MainActivity
import dev.manuel.proyectomoviles.R
import dev.manuel.proyectomoviles.databinding.FragmentRegistroBinding
import dev.manuel.proyectomoviles.databinding.FragmentTelefonoBinding
import dev.manuel.proyectomoviles.db.AppDatabase
import dev.manuel.proyectomoviles.setUserId
import java.util.*
import java.util.concurrent.TimeUnit

class FragmentTelefono : Fragment(), View.OnClickListener {

    private val firestore = AppDatabase.getDatabase()?.firestore
    private val auth = AppDatabase.getDatabase()?.auth

    val name = binding.txtNombre.editText?.editableText
    val username = binding.txtUsername.editText?.editableText
    val password = binding.VerificationCode.editText?.editableText
    val numero = binding.phoneEditText.editText?.editableText
    val btnVolver = binding.btnVolver
    var storedVerificationId:String=""

    val btnVerificar = binding.btnVerificar
    val btnMandar = binding.btnMandar

    val TAG = "FragmentTelefono"

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

        btnVerificar.setOnClickListener(this)
        btnMandar.setOnClickListener(this)

        btnVolver.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentTelefono_to_fragmentLogin)
        }

        btnMandar.setOnClickListener {

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

    override fun onClick(p0: View?) {
        if (p0==btnMandar){
            val phoneNumber:String=numero.toString()
            val options = PhoneAuthOptions.newBuilder(auth!!)
                .setPhoneNumber(phoneNumber)       // Phone number to verify
                .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(requireActivity())                 // Activity (for callback binding)
                .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                .build()
            auth.setLanguageCode(Locale.getDefault().language)
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
        else if(p0==btnVerificar){

        }
    }
    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d(TAG, "onVerificationCompleted:$credential")

            //signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(TAG, "onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:$verificationId")

            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
            //resendToken = token
        }
    }
}