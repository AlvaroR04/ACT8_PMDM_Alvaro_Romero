package auxiliar

import kotlin.random.Random

object Factorias {

    fun factoriaEnlaceImagen() : String {
        val imagenes = listOf("https://loremflickr.com/g/320/240/paris","https://loremflickr.com/320/240/dog","https://loremflickr.com/320/240","https://loremflickr.com/320/240/brazil,rio", "https://loremflickr.com/320/240/paris,girl/all")
        return imagenes.get(Random.nextInt(0, imagenes.size))
    }
}