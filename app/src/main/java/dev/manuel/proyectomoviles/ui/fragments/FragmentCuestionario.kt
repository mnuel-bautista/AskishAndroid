package dev.manuel.proyectomoviles.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.manuel.proyectomoviles.R
import dev.manuel.proyectomoviles.models.PreguntasModel
import dev.manuel.proyectomoviles.ui.fragments.adapters.AdaptadorPreguntas


class FragmentCuestionario : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_cuestionario, container, false)
        val recycler : RecyclerView = root.findViewById(R.id.listaPreguntas)
        val adapter : AdaptadorPreguntas = AdaptadorPreguntas()

        //Configuracion del adapter, aqui se mandan datos de firebase
        adapter.AdaptadorPreguntas(pregunt(), requireContext())

        //Configuracion del recycler
        recycler.hasFixedSize()
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        return root
    }

    private fun pregunt(): MutableList<PreguntasModel> {
        var preguntasModels : MutableList<PreguntasModel> = ArrayList()
        preguntasModels.add(PreguntasModel("¿Quien descubrio America?", "Cristobal Colon", "jajajsadpsaldpsaldpsldpsalpldaslpasldpsaldsaldsapdaspld"))
        preguntasModels.add(PreguntasModel("¿Quien descubrio America?", "Cristobal Colon", "jajajsadpsaldpsaldpsldpsalpldaslpasldpsaldsaldsapdaspld"))
        preguntasModels.add(PreguntasModel("¿Quien descubrio America?", "Cristobal Colon", "jajajsadpsaldpsaldpsldpsalpldaslpasldpsaldsaldsapdaspld"))
        preguntasModels.add(PreguntasModel("¿Quien descubrio America?", "Cristobal Colon", "jajajsadpsaldpsaldpsldpsalpldaslpasldpsaldsaldsapdaspld"))
        preguntasModels.add(PreguntasModel("¿Quien descubrio America?", "Cristobal Colon", "jajajsadpsaldpsaldpsldpsalpldaslpasldpsaldsaldsapdaspld"))
        preguntasModels.add(PreguntasModel("¿Quien descubrio America?", "Cristobal Colon", "jajajsadpsaldpsaldpsldpsalpldaslpasldpsaldsaldsapdaspld"))
        preguntasModels.add(PreguntasModel("¿Quien descubrio America?", "Cristobal Colon", "jajajsadpsaldpsaldpsldpsalpldaslpasldpsaldsaldsapdaspld"))
        preguntasModels.add(PreguntasModel("¿Quien descubrio America?", "Cristobal Colon", "jajajsadpsaldpsaldpsldpsalpldaslpasldpsaldsaldsapdaspld"))
        preguntasModels.add(PreguntasModel("¿Quien descubrio America?", "Cristobal Colon", "jajajsadpsaldpsaldpsldpsalpldaslpasldpsaldsaldsapdaspld"))
        preguntasModels.add(PreguntasModel("¿Quien descubrio America?", "Cristobal Colon", "jajajsadpsaldpsaldpsldpsalpldaslpasldpsaldsaldsapdaspld"))
        preguntasModels.add(PreguntasModel("¿Quien descubrio America?", "Cristobal Colon", "jajajsadpsaldpsaldpsldpsalpldaslpasldpsaldsaldsapdaspld"))

        return preguntasModels
    }
}