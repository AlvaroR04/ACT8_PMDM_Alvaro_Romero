package com.alvaror04.act8_alvaro_romero

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alvaror04.act8_alvaro_romero.databinding.ActivityResumenBinding

class Resumen : AppCompatActivity() {

    lateinit var binding : ActivityResumenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResumenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvEncuestas.text = intent.getStringExtra("encuestados")

        binding.bVolver.setOnClickListener {
            finish()
        }
    }
}