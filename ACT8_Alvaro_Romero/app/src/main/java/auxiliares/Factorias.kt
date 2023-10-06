package auxiliares

import android.content.Context
import kotlin.random.Random

object Factorias {

    /**
     * Obtiene un enlace de una imagen aleatoria alojada en un servidor
     *
     * @return enlace de una imagen aleatoria de una lista de imagenes
     */
    fun factoriaEnlaceImagen(): String {
        val imagenes = listOf (
            "https://loremflickr.com/g/320/240/paris",
            "https://loremflickr.com/320/240/dog",
            "https://loremflickr.com/320/240",
            "https://loremflickr.com/320/240/brazil,rio",
            "https://loremflickr.com/320/240/paris,girl/all"
        )

        return imagenes.get(Random.nextInt(0, imagenes.size))
    }

    /**
     * Obtiene una ID autogenerada que no este repetida
     *
     * @param contexto el contexto de de la actividad u elemento
     * @return ID autogenerada y no repetida
     */
    fun factoriaID(contexto: Context): Int {
        var id = 0

        do { //Si existe la ID, vuelve a generar una
            id = Math.abs(Random.nextInt())
        } while(Conexion.existeID(contexto, id))

        return id
    }
}