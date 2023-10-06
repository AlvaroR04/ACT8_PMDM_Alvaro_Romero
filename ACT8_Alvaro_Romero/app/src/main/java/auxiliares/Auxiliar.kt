package auxiliares

import android.content.Context
import android.widget.Toast

object Auxiliar {

    /**
     * Su proposito es simplificar el proceso de creacion de un toast
     *
     * @param contexto el contexto de una actividad u elemento
     * @param msg es el mensaje que se quiere comunicar mediante el toast
     */
    fun mostrarToast(contexto: Context, msg: String) {
        Toast.makeText(
            contexto,
            msg,
            Toast.LENGTH_LONG
        ).show()
    }

}