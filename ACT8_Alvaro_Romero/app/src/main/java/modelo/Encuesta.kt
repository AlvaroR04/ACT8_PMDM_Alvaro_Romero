package modelo

import java.io.Serializable

data class Encuesta (
    val id : Int,
    val nombre : String,
    val so : String,
    val especialidades : ArrayList<String>,
    val horasEstudio : Int,
    val imagen : String
) : Serializable
