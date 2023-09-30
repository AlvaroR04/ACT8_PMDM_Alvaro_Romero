package com.alvaror04.act8_alvaro_romero

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import auxiliar.Factorias
import com.alvaror04.act8_alvaro_romero.databinding.ActivityMainBinding
import modelo.Almacen
import modelo.Encuesta
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val grpOs = listOf(binding.rbWindows, binding.rbMac, binding.rbLinux)
        val grpEsp = listOf(binding.cbDam, binding.cbDaw, binding.cbAsir)

        binding.sldHoras.addOnChangeListener { slider, valor, esUsuario ->
            binding.tvHoras.text = valor.roundToInt().toString()
        }

        binding.swAnonimo.setOnClickListener {
            binding.edNombre.isEnabled = !binding.swAnonimo.isChecked
        }

        binding.bReiniciar.setOnClickListener {
            finish()
            startActivity(intent)
        }

        binding.bCuantas.setOnClickListener {
            var participacion = if(Almacen.encuestas.size == 1)
                "${Almacen.encuestas.size} ${application.getString(R.string.person_participated)}" else
                "${Almacen.encuestas.size} ${application.getString(R.string.people_participated)}"

            mostrarToast(participacion)
        }

        binding.bValidar.setOnClickListener {
            if(binding.edNombre.text!!.isNotEmpty() || binding.swAnonimo.isChecked) {
                val encuestado: Encuesta

                var so = ""
                val nombre = if(binding.swAnonimo.isChecked)
                    application.getString(R.string.anonymous) else
                    binding.edNombre.text.toString()
                val especialidades = ArrayList<String>()
                val imagen = if(binding.swAnonimo.isChecked) "anonimo"
                    else Factorias.factoriaEnlaceImagen()

                val abrirResumen = Intent(this, Resumen::class.java)

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

                encuestado = Encuesta(nombre.trim(), so, especialidades, binding.sldHoras.value.roundToInt(), imagen)

                Almacen.encuestas.add (encuestado)

                abrirResumen.putExtra("encuestado", encuestado)
                startActivity(abrirResumen)
            } else {
                mostrarToast(application.getString(R.string.err_NoName))
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