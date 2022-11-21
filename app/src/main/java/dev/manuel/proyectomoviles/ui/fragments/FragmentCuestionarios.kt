package dev.manuel.proyectomoviles.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.ListFragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import dev.manuel.proyectomoviles.R


class FragmentCuestionarios : Fragment(){

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

        lv.setOnItemClickListener { adapterView, view, i, l ->
            if (i == 0){

//                val fragmentCuesti = FragmentCuestionario()
//                val transaction = fragmentManager?.beginTransaction()
//                transaction?.replace(R.id.ejemplo,fragmentCuesti)?.commit()
                Toast.makeText(requireContext(), "HOLAAAAA", Toast.LENGTH_SHORT).show()
                parentFragmentManager.commit {
                    replace<FragmentCuestionario>(R.id.layoutCuestionarios)
                    setReorderingAllowed(true)
                    addToBackStack(null)
                }
            }



        }



        return root
    }

}