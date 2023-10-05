package auxiliares

import android.content.Context
import android.widget.Toast

object Auxiliar {
    fun mostrarToast(contexto: Context, msg: String) {
        Toast.makeText(
            contexto,
            msg,
            Toast.LENGTH_LONG
        ).show()
    }

}