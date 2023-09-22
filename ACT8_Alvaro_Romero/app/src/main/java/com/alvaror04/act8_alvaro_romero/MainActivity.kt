package com.alvaror04.act8_alvaro_romero

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.SeekBar
import android.widget.Toast
import com.alvaror04.act8_alvaro_romero.databinding.ActivityMainBinding
import modelo.Encuesta

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val encuestas = ArrayList<Encuesta>()

        val grpOs = listOf(binding.rbWindows, binding.rbMac, binding.rbLinux)
        val grpEsp = listOf(binding.cbDam, binding.cbDaw, binding.cbAsir)

        binding.sbHoras.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                binding.tvHoras.text = binding.sbHoras.progress.toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })

        binding.swAnonimo.setOnClickListener {
            binding.edNombre.isEnabled = !binding.swAnonimo.isChecked
        }

        binding.bReiniciar.setOnClickListener {
            finish()
            startActivity(intent)
        }

        binding.bCuantas.setOnClickListener {
            var participacion = if(encuestas.size == 1)
                "${encuestas.size} ${application.getString(R.string.person_participated)}" else
                "${encuestas.size} ${application.getString(R.string.people_participated)}"

            mostrarToast(participacion)
        }

        binding.bResumen.setOnClickListener {
            binding.tvResumen.text = ""

            for(e in encuestas) {
                binding.tvResumen.append(e.toString() + "\n")
            }
        }

        binding.bValidar.setOnClickListener {
            if(binding.edNombre.text.isNotEmpty() || binding.swAnonimo.isChecked) {
                var so = ""
                val nombre = if(binding.swAnonimo.isChecked)
                    application.getString(R.string.anonymous) else
                    binding.edNombre.text.toString()
                val especialidades = ArrayList<String>()

                var i = 0
                var estaSeleccionado = false
                while(i < grpOs.size && !estaSeleccionado) {
                    estaSeleccionado = grpOs.get(i).isChecked
                    i++
                }
                so = grpOs.get(i - 1).text.toString()

                for(e in grpEsp) {
                    if(e.isChecked) {
                        especialidades.add(e.text.toString())
                    }
                }

                encuestas.add (
                    Encuesta(nombre, so, especialidades, binding.sbHoras.progress)
                )
            }
        }
    }

    fun mostrarToast(msg: String) {
        Toast.makeText(
            this,
            msg,
            Toast.LENGTH_LONG
        ).show()
    }
}