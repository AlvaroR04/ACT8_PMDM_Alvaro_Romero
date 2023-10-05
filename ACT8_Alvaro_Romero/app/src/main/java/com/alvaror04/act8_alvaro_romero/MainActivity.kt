package com.alvaror04.act8_alvaro_romero

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import auxiliares.Auxiliar
import auxiliares.Conexion
import auxiliares.Factorias
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

        //Agrupa los checkbox y radiobotones en su categoria correspondiente
        val grpSo = listOf(binding.rbWindows, binding.rbMac, binding.rbLinux)
        val grpEsp = listOf(binding.cbDam, binding.cbDaw, binding.cbAsir)

        obtenerEncuestas()

        //Acciones con los controles de la actividad
        binding.sldHoras.addOnChangeListener { slider, valor, esUsuario ->
            binding.tvHoras.text = valor.roundToInt().toString()
        }

        binding.swAnonimo.setOnClickListener {
            //El campo de texto del nombre estara disponible mientras el interruptor de anonimo no este activo
            binding.edNombre.isEnabled = !binding.swAnonimo.isChecked
        }

        binding.bReiniciar.setOnClickListener {
            //Finaliza la actividad y la vuelve a iniciar
            finish()
            startActivity(intent)
        }

        binding.bCuantas.setOnClickListener {
            //Si es una participacion, usa el singular. Sino, el plural
            val participacion = if(Almacen.encuestas.size == 1)
                "${Almacen.encuestas.size} ${application.getString(R.string.person_participated)}" else
                "${Almacen.encuestas.size} ${application.getString(R.string.people_participated)}"

            Auxiliar.mostrarToast (
                this,
                participacion
            )
        }

        binding.bValidar.setOnClickListener {
            //Si el campo de texto no esta vacio o el interruptor de anonimo esta marcado
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

                //Obten el sistema operativo seleccionado. Sal del bucle cuando lo encuentres
                var i = 0
                var estaSeleccionado = false
                while(i < grpSo.size && !estaSeleccionado) {
                    estaSeleccionado = grpSo.get(i).isChecked
                    i++
                }
                so = grpSo.get(i - 1).text.toString()

                //Obten las especialidades marcadas
                for(e in grpEsp) {
                    if(e.isChecked) {
                        especialidades.add(e.text.toString())
                    }
                }

                //Crea la encuesta a partir de los datos pasados
                encuestado = Encuesta (
                    Factorias.factoriaID(this),
                    nombre.trim(),
                    so,
                    especialidades,
                    binding.sldHoras.value.roundToInt(),
                    imagen
                )

                //Agrega la encuesta al vector y a la base de datos interna
                Almacen.encuestas.add(encuestado)
                Conexion.agregarEncuesta(this, encuestado)

                //Envia la encuesta a la siguiente actividad
                abrirResumen.putExtra("encuestado", encuestado)
                startActivity(abrirResumen)
            } else {
                Auxiliar.mostrarToast (
                    this,
                    application.getString(R.string.err_NoName)
                )
            }
        }
    }

    fun obtenerEncuestas() {
        val obtEncuestas = Conexion.obtenerEncuestas(this) //Obten las encuestas ya registradas si existen
        if(obtEncuestas.size > 0) {
            Almacen.encuestas = obtEncuestas
        }
    }
}