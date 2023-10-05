package adaptadores

import modelo.*
import android.app.*
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import auxiliares.Auxiliar
import auxiliares.Conexion
import com.bumptech.glide.Glide
import com.alvaror04.act8_alvaro_romero.*

class MiAdaptadorRecycler (var encuestados : ArrayList<Encuesta>, var  context: Context) : RecyclerView.Adapter<MiAdaptadorRecycler.ViewHolder>(){

    companion object {
        //Esta variable estática nos será muy útil para saber cual está marcado o no.
        var seleccionado:Int = -1
        /*
        PAra marcar o desmarcar un elemento de la lista lo haremos diferente a una listView. En la listView el listener
        está en la activity por lo que podemos controlar desde fuera el valor de seleccionado y pasarlo al adapter, asociamos
        el adapter a la listview y resuelto.
        En las RecyclerView usamos para pintar cada elemento la función bind (ver código más abajo, en la clase ViewHolder).
        Esto se carga una vez, solo una vez, de ahí la eficiencia de las RecyclerView. Si queremos que el click que hagamos
        se vea reflejado debemos recargar la lista, para ello forzamos la recarga con el método: notifyDataSetChanged().
         */
    }


    /**
     * onBindViewHolder() se encarga de coger cada una de las posiciones de la lista de personajes y pasarlas a la clase
     * ViewHolder(clase interna, ver abajo) para que esta pinte todos los valores y active el evento onClick en cada uno.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = encuestados.get(position)
        holder.bind(item, context, position, this)
    }

    /**
     *  Como su nombre indica lo que hará será devolvernos un objeto ViewHolder al cual le pasamos la celda que hemos creado.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val layoutInflater = LayoutInflater.from(parent.context)
//        //return ViewHolder(layoutInflater.inflate(R.layout.item_lo,parent,false))
//        return ViewHolder(layoutInflater.inflate(R.layout.item_card,parent,false))
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        val viewHolder = ViewHolder(vista)
        // Configurar el OnClickListener
        viewHolder.itemView.setOnClickListener {
            val intent = Intent(context, Resumen::class.java)
            context.startActivity(intent)
        }

        return viewHolder
    }

    /**
     * getItemCount() nos devuelve el tamaño de la lista, que lo necesita el RecyclerView.
     */
    override fun getItemCount(): Int = encuestados.size


    //--------------------------------- Clase interna ViewHolder -----------------------------------
    /**
     * La clase ViewHolder. No es necesaria hacerla dentro del adapter, pero como van tan ligadas
     * se puede declarar aquí.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //Esto solo se asocia la primera vez que se llama a la clase, en el método onCreate de la clase que contiene a esta.
        //Por eso no hace falta que hagamos lo que hacíamos en el método getView de los adaptadores para las listsViews.
        //val nombrePersonaje = view.findViewById(R.id.txtNombre) as TextView
        //val tipoPersonaje = view.findViewById(R.id.txtTipo) as TextView
        //val avatar = view.findViewById(R.id.imgImagen) as ImageView

        //Como en el ejemplo general de las listas (ProbandoListas) vemos que se puede inflar cada elemento con una card o con un layout.

        //Qué pena que no pueda usar binding aquí... Al menos, no de momento.
        val nombre = view.findViewById(R.id.tv_Nombre) as TextView
        val so = view.findViewById(R.id.tv_SO) as TextView
        val horas = view.findViewById(R.id.tv_HorasEstudio) as TextView
        val avatar = view.findViewById(R.id.iv_Imagen) as ImageView

        val b_Detalle = view.findViewById(R.id.b_Detalle) as Button
        /**
         * Éste método se llama desde el método onBindViewHolder de la clase contenedora. Como no vuelve a crear un objeto
         * sino que usa el ya creado en onCreateViewHolder, las asociaciones findViewById no vuelven a hacerse y es más eficiente.
         */
        @SuppressLint("ResourceAsColor")
        fun bind(encuestado: Encuesta, context: Context, pos: Int, miAdaptadorRecycler: MiAdaptadorRecycler){
            nombre.text = encuestado.nombre
            so.text = encuestado.so

            horas.text = if(encuestado.horasEstudio == 1) "${encuestado.horasEstudio} ${context.getString(R.string.hour_study).lowercase()}"
                else "${encuestado.horasEstudio} ${context.getString(R.string.hours_study).lowercase()}"

            if (encuestado.nombre.equals(context.getString(R.string.anonymous))){
                //Para anonimo, como ejemplo, le he puesto una imagen que se llama igual en el drawable.
                //se podría hacer igual para los otros personajes.
                val uri = "@drawable/" + encuestado.imagen
                val imageResource: Int = context.getResources().getIdentifier(uri, null, context.getPackageName())
                var res: Drawable = context.resources.getDrawable(imageResource)
                avatar.setImageDrawable(res)
            }
            else {
                Glide.with(context)
                    .load (
                        encuestado.imagen
                    )
                    .into(avatar)

                /*
                Para que funcione Glide se debe poner esta dependencia en build.gradle y sincronizar el proyecto:
                implementation "com.github.bumptech.glide:glide:4.11.0"
                 */
                //Picasso.with(context).load(url).into(this) //--> Esta es otra librería que hace lo mismo que Glide. Algo más antigua pero vigente.
            }

            //Para marcar o desmarcar al seleccionado usamos el siguiente código.
            if (pos == seleccionado) {
                with(nombre) {
                    this.setTextColor(resources.getColor(R.color.purple))
                }
                so.setTextColor(context.getColor(R.color.purple))
                horas.setTextColor(context.getColor(R.color.purple))
            }
            else {
                with(nombre) {
                    this.setTextColor(resources.getColor(R.color.black))
                }
                so.setTextColor(context.getColor(R.color.grey))
                horas.setTextColor(context.getColor(R.color.grey))
            }

//            itemView.setOnLongClickListener(View.OnLongClickListener() {
//                Log.e("Fernando","long click")
//            }

            //Se levanta una escucha para cada item. Si pulsamos el seleccionado pondremos la selección a -1, en otro caso será el nuevo sleccionado.
            itemView.setOnClickListener {
                if (pos == MiAdaptadorRecycler.seleccionado){
                    MiAdaptadorRecycler.seleccionado = -1
                } else {
                    MiAdaptadorRecycler.seleccionado = pos
                    Log.e("Fernando", "Seleccionado: ${Almacen.encuestas.get(MiAdaptadorRecycler.seleccionado).toString()}")
                }
                //Con la siguiente instrucción forzamos a recargar el viewHolder porque han cambiado los datos. Así pintará al seleccionado.
                miAdaptadorRecycler.notifyDataSetChanged()

                Auxiliar.mostrarToast(
                    context,
                    "${context.getString(R.string.element_deleted)} ${seleccionado}"
                )
            }

            itemView.setOnLongClickListener {
                val resPositiva = { dialogo: DialogInterface, which: Int ->

                    if(Conexion.borrarEncuesta(context, Almacen.encuestas.get(pos).id) == 1) {
                        Almacen.encuestas.removeAt(pos)
                        Auxiliar.mostrarToast(
                            context,
                            context.getString(R.string.element_deleted)
                        )
                    } else {
                        Auxiliar.mostrarToast(
                            context,
                            context.getString(R.string.err_Delete)
                        )
                    }

                    miAdaptadorRecycler.notifyDataSetChanged()
                }

                val resNegativa = {dialogo: DialogInterface, which: Int ->
                    Toast.makeText (
                        context,
                        "Acción cancelada",
                        Toast.LENGTH_LONG
                    ).show()
                }

                val builder = AlertDialog.Builder(context)

                with(builder) {
                    setTitle("Eliminar encuesta")
                    setMessage("¿Desea eliminar esta encuesta?")
                    setPositiveButton("Sí", resPositiva)
                    setNegativeButton("No", resNegativa)
                    show()
                }

                true
            }

            b_Detalle.setOnClickListener {
                val builder = AlertDialog.Builder(context)

                with(builder) {
                    setTitle("Datos de ${encuestado.nombre}")
                    setMessage (
                        "· ID: ${encuestado.id}\n\n" +
                        "· ${context.getString(R.string.favourite_os)}: ${encuestado.so}\n\n" +
                        "· ${context.getString(R.string.speciality)}: ${encuestado.especialidades}\n\n" +
                        "· ${context.getString(R.string.hours_study)}: ${encuestado.horasEstudio}"
                    )
                    setPositiveButton("Aceptar", null)
                    show()
                }
            }

        }
    }
}
