package fabiola.deleon.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class activity_login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btninicio = findViewById<Button>(R.id.btninicio)
        val  txtcontrasena = findViewById<TextView>(R.id.txtcontra)
        val txtcorreo = findViewById<TextView>(R.id.txtcorreo)
        val lbResgistro = findViewById<TextView>(R.id.lbRegistro)

        lbResgistro.setOnClickListener{
            val pantallaRegistro = Intent(this, activity_registro::class.java)
            startActivity(pantallaRegistro)

            finish()
        }

        btninicio.setOnClickListener {
            val pantallaInicio = Intent(this, MainActivity::class.java)

            GlobalScope.launch (Dispatchers.IO ){

            val objconexion = Modelo.conexion().cadenaconexion()
            val comprobarUsuario = objconexion?.prepareStatement("SELECT * FROM TB_USUARIO WHERE Correo = ? AND Contraseña = ?")!!
            comprobarUsuario.setString(1, txtcorreo.text.toString())
            comprobarUsuario.setString(2, txtcontrasena.text.toString())

            val resultado = comprobarUsuario.executeQuery()

            if (resultado.next()){
                startActivity(pantallaInicio)
            }
            else{
                withContext(Dispatchers.Main){
                    Toast.makeText(this@activity_login, "Credenciales inválidas", Toast.LENGTH_SHORT).show()
                }

            }
        }

    }
}
}