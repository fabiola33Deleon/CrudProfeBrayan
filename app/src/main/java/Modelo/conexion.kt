package Modelo

import java.sql.DriverManager
import java.sql.Connection

class conexion {
    fun cadenaconexion(): Connection? {

        return try{
            val url = "jdbc:oracle:thin:@192.168.0.107:1521:xe"
            val user = "SYSTEM"
            val password = "milkshake"

            val Connection = DriverManager.getConnection(url, user, password)
            return Connection
        }
        catch (e:Exception){
            println("Este es el error:$e")
            null
        }
    }
}