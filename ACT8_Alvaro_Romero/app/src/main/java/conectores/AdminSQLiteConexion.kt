package conectores

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AdminSQLiteConexion(contexto: Context, nombre: String, factoria: SQLiteDatabase.CursorFactory?, version: Int): SQLiteOpenHelper(contexto, nombre, factoria, version) {
    override fun onCreate(bbdd: SQLiteDatabase) {
        bbdd.execSQL("CREATE TABLE ENCUESTA (\n" +
                "    id INT PRIMARY KEY,\n" +
                "    nombre TEXT,\n" +
                "    so TEXT,\n" +
                "    horas_estudio INT,\n" +
                "    imagen TEXT\n" +
                ")"
        )

        bbdd.execSQL("CREATE TABLE ESPECIALIDADES (\n" +
                "    nombre TEXT PRIMARY KEY\n" +
                ")"
        )

        bbdd.execSQL("CREATE TABLE ENC_ESP(\n" +
                "    id_encuesta INT,\n" +
                "    nombre_esp TEXT,\n" +
                "    FOREIGN KEY (id_encuesta) REFERENCES ENCUESTA(id),\n" +
                "    FOREIGN KEY (nombre_esp) REFERENCES ESPECIALIDADES(nombre)\n" +
                ")"
        )

        bbdd.execSQL("INSERT INTO ESPECIALIDADES VALUES('DAM')")
        bbdd.execSQL("INSERT INTO ESPECIALIDADES VALUES('DAW')")
        bbdd.execSQL("INSERT INTO ESPECIALIDADES VALUES('ASIR')")
    }

    override fun onUpgrade(bbdd: SQLiteDatabase, verAnt: Int, verPos: Int) {

    }

}