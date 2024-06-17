package Modelo

import oracle.sql.DATE
import java.util.Date

data class DataClassTicket(
    var numTicket: Int,
    var titulo: String,
    var descripcion: String,
    var autor: String,
    var correo: String,
    var fechaCreacion: Date,
    var estado: String,
    var fechaFinalizacion: String
)
