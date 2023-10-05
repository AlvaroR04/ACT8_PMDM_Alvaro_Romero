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

        registro.put("id", e.id)
        registro.put("nombre", e.nombre)
        registro.put("so", e.so)
        registro.put("horas_estudio", e.horasEstudio)
        registro.put("imagen", e.imagen)

        bd.insert("ENCUESTA", null, registro)

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

        val filaEncuesta = bd.rawQuery (
            "SELECT id, nombre, so, horas_estudio, imagen FROM ENCUESTA",
            null
        )
        while(filaEncuesta.moveToNext()) {
            val especialidades = ArrayList<String>()
            val filaEsp = bd.rawQuery (
                "SELECT nombre_esp FROM ENC_ESP WHERE id_encuesta = ${filaEncuesta.getInt(0)}",
                null
            )

            while(filaEsp.moveToNext()) {
                especialidades.add(filaEsp.getString(0))
            }

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
        var existe = false
        val admin = AdminSQLiteConexion(contexto, bbdd, null, 1)
        val bd = admin.writableDatabase
        val fila = bd.rawQuery (
            "SELECT id FROM ENCUESTA",
            null
        )

        while(fila.moveToNext() && !existe) {
            existe = (id == fila.getInt(0))
        }
        bd.close()

        return existe
    }
}