package fabiola.deleon.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class activity_registro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtnombre = findViewById<EditText>(R.id.txtnombre)
        val txtApellido = findViewById<EditText>(R.id.txtApellido)
        val txtemail = findViewById<EditText>(R.id.txtemail)
        val txtcontraseña = findViewById<EditText>(R.id.txtcontraseña)
        val btnRegistrarse = findViewById<Button>(R.id.btnRegistrarse)
        val ic_icecream = findViewById<ImageView>(R.id.icIcecream)

        ic_icecream.setOnClickListener {
            val activityLogin = Intent( this, activity_login::class.java)
            startActivity(activityLogin)
            finish()
        }

        btnRegistrarse.setOnClickListener {
            try{
                val activityLogin = Intent(this, activity_login::class.java)
                GlobalScope.launch(Dispatchers.IO) {
                    val objConexion = Modelo.conexion().cadenaconexion()
                    val crearUsuario = objConexion?.prepareStatement("INSERT INTO TB_USUARIO (Nombre, Apellido, Correo, Contraseña) VALUES (?,?,?,?)")!!
                    crearUsuario.setString(1, txtnombre.text.toString())
                    crearUsuario.setString( 2, txtApellido.text.toString())
                    crearUsuario.setString(3, txtemail.text.toString())
                    crearUsuario.setString(4, txtcontraseña.text.toString())
                    crearUsuario.executeUpdate()

                    withContext(Dispatchers.Main){
                        Toast.makeText(this@activity_registro,"Se creo tu usuario", Toast.LENGTH_SHORT).show()

                        startActivity(activityLogin)
                        finish()
                    }
                }
            }
            catch (ex:Exception){
                println("Register: aqui esta ell error:$ex")
            }
        }

    }
}