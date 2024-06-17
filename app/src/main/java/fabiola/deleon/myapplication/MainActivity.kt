package fabiola.deleon.myapplication

import Modelo.DataClassTicket
import Modelo.conexion
import RecyclerViewHelper.Adaptador
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fabiola.deleon.myapplication.R.id.txtCorreo
import fabiola.deleon.myapplication.R.id.txtfecha
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val txtname = findViewById<EditText>(R.id.txtname)
        val txtdescripcion= findViewById<EditText>(R.id.txtdescripcion)
        val txtsunombre= findViewById<EditText>(R.id.txtSunombre)
        val txtCorreo = findViewById<EditText>(R.id.txtCorreo)
        val txtleurge = findViewById<EditText>(R.id.txtleurge)
        val txtfecha = findViewById<EditText>(txtfecha)
        val btncrear = findViewById<Button>(R.id.btnCrear)
        val rcvItems= findViewById<RecyclerView>(R.id.rcvitems)

        rcvItems.layoutManager =  LinearLayoutManager(this)


        fun obtenerDatos(): List<DataClassTicket> {
            val tickets = mutableListOf<DataClassTicket>()
            val objConexion = Modelo.conexion().cadenaconexion()

            objConexion?.use { connection ->
                val statement = connection.createStatement()
                val resultSet = statement.executeQuery("SELECT * FROM TB_TICKET")

                resultSet.use { rs ->
                    while (rs.next()) {
                        val numTicket = rs.getInt("NUM_TICKET")
                        val titulo = rs.getString("TÍTULO")
                        val descripcion = rs.getString("DESCRIPCIÓN")
                        val autor = rs.getString("AUTOR")
                        val correo = rs.getString("CORREO")
                        val fechaCreacion = rs.getDate("FECHA_CREACIÓN")
                        val estado = rs.getString("ESTADO")
                        val fechaFinalizacion = rs.getString("FECHA_FINALIZACIÓN")

                        val ticket = DataClassTicket (numTicket, titulo, descripcion, autor, correo, fechaCreacion, estado, fechaFinalizacion)
                        tickets.add(ticket)
                    }
                }
            }
            return tickets
        }

        CoroutineScope(Dispatchers.IO).launch {
            val ticketsBd = obtenerDatos()
            withContext(Dispatchers.Main) {
                val miAdapter = Adaptador(ticketsBd)
                rcvItems.adapter = miAdapter
            }
        }
        btncrear.setOnClickListener {
            try {
                CoroutineScope(Dispatchers.IO).launch {
                    val objConexion = conexion().cadenaconexion()

                    objConexion?.use { connection ->
                        val crearTicket = connection.prepareStatement("INSERT INTO TB_TICKET (título, descripción, autor, correo, estado, Fecha_Finalización) VALUES (?, ?, ?, ?, ?, ?)")

                        crearTicket.setString(1, txtname.text.toString())
                        crearTicket.setString(2, txtdescripcion.text.toString())
                        crearTicket.setString(3, txtsunombre.text.toString())
                        crearTicket.setString(4, txtCorreo.text.toString())
                        crearTicket.setString(5, txtleurge.text.toString())
                        crearTicket.setString(6, txtfecha.text.toString())
                        crearTicket.executeUpdate()
                    }

                    val nuevoTicket = obtenerDatos()
                    withContext(Dispatchers.Main){
                        (rcvItems.adapter as? Adaptador)?.actualizarLista(nuevoTicket)
                    }
                }
            } catch (ex: Exception) {
                println("REGISTER: este es el error: $ex")
            }
        }



    }

    }
