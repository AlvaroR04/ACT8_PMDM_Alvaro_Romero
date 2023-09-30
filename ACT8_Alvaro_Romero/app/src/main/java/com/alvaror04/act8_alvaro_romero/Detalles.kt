package com.alvaror04.act8_alvaro_romero

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alvaror04.act8_alvaro_romero.databinding.ActivityDetallesBinding
import modelo.Encuesta

class Detalles : AppCompatActivity() {

    lateinit var binding: ActivityDetallesBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetallesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val persona = intent.getSerializableExtra("encSelec") as Encuesta

        binding.tvInformacion.text = "[${persona.nombre}]\n" +
                "\t${application.getString(R.string.favourite_os)}: ${persona.so}\n" +
                "\t${application.getString(R.string.speciality)}: ${persona.especialidades}\n" +
                "\t${application.getString(R.string.hours_study)}: ${persona.horasEstudio}"

        binding.bVolver.setOnClickListener {
            finish()
        }
    }
}