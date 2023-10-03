package com.alvaror04.act8_alvaro_romero

import adaptadores.MiAdaptadorRecycler
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alvaror04.act8_alvaro_romero.databinding.ActivityResumenBinding
import modelo.Almacen
import modelo.Encuesta

class Resumen : AppCompatActivity() {

    lateinit var binding : ActivityResumenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResumenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val persona = intent.getSerializableExtra("encuestado") as Encuesta

        binding.tvGracias.text = if(!persona.nombre.equals(application.getString(R.string.anonymous)))
            "${application.getString(R.string.thank_you)}, ${persona.nombre}, ${application.getString(R.string.answer_registered)}" else
            "${application.getString(R.string.thank_you)}, ${application.getString(R.string.answer_registered)}"

        binding.rvEncuestados.setHasFixedSize(true)
        binding.rvEncuestados.layoutManager = LinearLayoutManager(this)
        val miAdapter = MiAdaptadorRecycler(Almacen.encuestas, this)
        binding.rvEncuestados.adapter = miAdapter

        binding.bVolver.setOnClickListener {
            finish()
        }
    }
}