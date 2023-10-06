package auxiliares

import android.content.ContentValues
import android.content.Context
import conectores.AdminSQLiteConexion
import modelo.Almacen
import modelo.Encuesta
import kotlin.random.Random

object Conexion {
    val bbdd = "encuestas.db3"

    fun agregarEncuesta(contexto: Context, e: Encuesta) {
        val admin = AdminSQLiteConexion(contexto, bbdd, null, 1)
        val bd = admin.writableDatabase
        val registro = ContentValues()

        //Incluye los datos del usuario e insertalos en la tabla ENCUESTA

        registro.put("id", e.id)
        registro.put("nombre", e.nombre)
        registro.put("so", e.so)
        registro.put("horas_estudio", e.horasEstudio)
        registro.put("imagen", e.imagen)

        bd.insert("ENCUESTA", null, registro)

        /*
            Incluye e inserta cada especialidad en la tabla interrelacionada
            entre ENCUESTA y ESPECIALIDADES, ya que ambos tienen una
            cardinalidad N:M (muchos a muchos)
        */

        for(esp in e.especialidades) {
            val espRegistro = ContentValues()
            espRegistro.put("id_encuesta", e.id)
            espRegistro.put("nombre_esp", esp)

            bd.insert("ENC_ESP", null, espRegistro)
        }

        bd.close()
    }

    fun borrarEncuesta(contexto: Context, id: Int): Int {
        val admin = AdminSQLiteConexion(contexto, bbdd, null, 1)
        val bd = admin.writableDatabase

        /*
            Debido a que ON DELETE CASCADE no funcionaba correctamente,
            se opto por hacerlo de esta forma
         */

        var cod = bd.delete (
            "ENC_ESP",
            "id_encuesta=${id}",
            null
        )

        /*
            Si la anterior consulta ha funcionado, realiza
            la siguiente consulta de eliminacion.
        */

        if(cod == 1) {
            cod = bd.delete (
                "ENCUESTA",
                "id=${id}",
                null
            )
        }

        bd.close()

        return cod
    }

    fun obtenerEncuestas(contexto: Context):ArrayList<Encuesta> {
        val encuestas = ArrayList<Encuesta>()
        val admin = AdminSQLiteConexion(contexto, bbdd, null, 1)
        val bd = admin.writableDatabase

        //Obten las encuestas registradas
        val filaEncuesta = bd.rawQuery (
            "SELECT id, nombre, so, horas_estudio, imagen FROM ENCUESTA",
            null
        )

        //Recorre cada encuesta registrada
        while(filaEncuesta.moveToNext()) {
            val especialidades = ArrayList<String>()

            //A su vez, obten las especialidades del usuario actual
            val filaEsp = bd.rawQuery (
                "SELECT nombre_esp FROM ENC_ESP WHERE id_encuesta = ${filaEncuesta.getInt(0)}",
                null
            )

            //Recorre esas especialidades y agregalas al vector de especialidades
            while(filaEsp.moveToNext()) {
                especialidades.add(filaEsp.getString(0))
            }

            //Agrega los datos obtenidos al vector de encuestas
            encuestas.add (
                Encuesta (
                    filaEncuesta.getInt(0),
                    filaEncuesta.getString(1),
                    filaEncuesta.getString(2),
                    especialidades,
                    filaEncuesta.getInt(3),
                    filaEncuesta.getString(4)
                )
            )
        }

        bd.close()

        return encuestas
    }

    fun existeID(contexto: Context, id: Int): Boolean {
        var existe = false //Para comprobar si el ID ya existe
        val admin = AdminSQLiteConexion(contexto, bbdd, null, 1)
        val bd = admin.writableDatabase

        //Obten las encuestas de todos los usuarios

        val fila = bd.rawQuery (
            "SELECT id FROM ENCUESTA",
            null
        )

        //Mientras haya elementos y no exista el ID

        while(fila.moveToNext() && !existe) {
            //Comprueba que el ID seleccionado sea igual al pasado
            existe = (id == fila.getInt(0))
        }

        bd.close()

        return existe
    }
}