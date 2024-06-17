package RecyclerViewHelper

import Modelo.DataClassTicket
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import fabiola.deleon.myapplication.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Adaptador(var Datos: List<DataClassTicket>) : RecyclerView.Adapter<RecyclerViewHelper.ViewHolder>() {
      fun actualizarLista(nuevaLista: List<DataClassTicket>) {
          Datos = nuevaLista
          notifyDataSetChanged()
      }

      fun actualizarDatos(numTicket: Int, titulo: String) {
          val index = Datos.indexOfFirst { it.numTicket == numTicket }
          Datos[index].titulo = titulo
          notifyItemChanged(index)
      }

      override fun onCreateViewHolder(
          parent: ViewGroup,
          viewType: Int
      ): RecyclerViewHelper.ViewHolder {
          val vista = LayoutInflater.from(parent.context)
              .inflate(R.layout.activity_tickets, parent, false)
          return RecyclerViewHelper.ViewHolder(vista)
      }

      override fun getItemCount() = Datos.size

      override fun onBindViewHolder(holder: RecyclerViewHelper.ViewHolder, position: Int) {
          val item = Datos[position]
          holder.txtcard.text = item.titulo


          holder.imgborrar.setOnClickListener {

              val context = holder.itemView.context

              val builder = AlertDialog.Builder(context)

              builder.setTitle("¿estas seguro?")

              builder.setMessage("quieres eliminar el registro")

              builder.setPositiveButton("sip") { dialog, wich ->
                  eliminarTicket(item.titulo, position)
              }

              builder.setNegativeButton("no") { dialog, wich ->

              }

              val alertDialog = builder.create()


              alertDialog.show()

          }

          holder.imgedit.setOnClickListener {

              val context = holder.itemView.context


              val builder = AlertDialog.Builder(context)
              builder.setTitle("Editar nombre")

              val cuadritoNuevoNombre = EditText(context)
              cuadritoNuevoNombre.setHint(item.titulo)
              builder.setView(cuadritoNuevoNombre)


              builder.setPositiveButton("Actualizar ") { dialog, wich ->
                  ActualizarTicket(
                      cuadritoNuevoNombre.text.toString(),
                      item.numTicket
                  )

              }

              builder.setNegativeButton("Cancelar") { dialog, wich ->
                  dialog.dismiss()
              }

              val dialog = builder.create()
              dialog.show()
          }
      }


      fun eliminarTicket(titulo: String, position: Int) {

          val listadatos = Datos.toMutableList()
          listadatos.removeAt(position)

          GlobalScope.launch(Dispatchers.IO) {

              val objConexion = Modelo.conexion().cadenaconexion()

              val eliminarTic = objConexion?.prepareStatement("delete TB_TICKET where Título=?")!!
              eliminarTic.setString(1, titulo)
              eliminarTic.executeUpdate()

              val commit = objConexion.prepareStatement("commit")!!
              commit.executeUpdate()
          }
          Datos = listadatos.toList()
          notifyItemRemoved(position)
          notifyDataSetChanged()

      }

      fun ActualizarTicket(titulo: String, numTicket: Int) {


          GlobalScope.launch(Dispatchers.IO) {


              val objConexion = Modelo.conexion().cadenaconexion()


              val updateTicket =
                  objConexion?.prepareStatement("UPDATE TB_TICKET SET Título = ? WHERE Num_Ticket = ?")!!
              updateTicket.setString(1, titulo)
              updateTicket.setInt(2, numTicket)
              updateTicket.executeUpdate()

              val commit = objConexion.prepareStatement("commit")!!
              commit.executeUpdate()


              withContext(Dispatchers.Main) {
                  actualizarDatos(numTicket, titulo)
              }
          }
      }
  }
