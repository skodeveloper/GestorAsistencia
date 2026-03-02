package com.example.gestorasistencia.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "estudiante")
data class EstudianteEntity(
    @PrimaryKey
    val matricula_id: String,
    val nombre: String,
    val apellido_p: String,
    val apellido_m: String?, // Puede ser nulo según el diccionario
    val fecha_nacimiento: String,
    val estatus: Boolean, // 1 Activo, 0 Inactivo
    val sincronizado: Boolean // 1 Sincronizado, 0 Pendiente
)
