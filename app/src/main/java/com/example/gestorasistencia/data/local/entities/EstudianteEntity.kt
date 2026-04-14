package com.example.gestorasistencia.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "estudiante")
data class EstudianteEntity(
    @PrimaryKey
    @ColumnInfo(name = "matricula_id") val matriculaId: String,
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "apellido_p") val apellidoP: String,
    @ColumnInfo(name = "apellido_m") val apellidoM: String?,
    @ColumnInfo(name = "fecha_nacimiento") val fechaNacimiento: String,
    @ColumnInfo(name = "estatus") val estatus: Int = 1,
    @ColumnInfo(name = "sincronizado") val sincronizado: Int = 0,
    @ColumnInfo(name = "carrera_id") val carreraId: Int
)