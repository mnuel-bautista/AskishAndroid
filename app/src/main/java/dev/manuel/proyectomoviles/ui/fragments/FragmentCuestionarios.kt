package dev.manuel.proyectomoviles.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import dev.manuel.proyectomoviles.R


class FragmentCuestionarios : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    //EJEMPLO
    lateinit var arrayAdapter:ArrayAdapter<*>
    lateinit var lv:ListView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val cuestio = mutableListOf<String>("Historia", "Matematicas", "Programacion")
        val root = inflater.inflate(R.layout.fragment_cuestionarios, container, false)

        lv = root.findViewById(R.id.listaCuesti)
        arrayAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1, cuestio) //requireContext() porque es fragment
        lv.adapter = arrayAdapter
/*
        lv.setOnClickListener (){
            parentFragmentManager.commit {
                replace<FragmentCuestionario>(R.id.fragmentCuestionarios)
                setReorderingAllowed(true)
                addToBackStack("principal")
            }
        }
*/
        return root
    }



}