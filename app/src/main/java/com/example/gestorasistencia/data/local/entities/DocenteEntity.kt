package com.example.gestorasistencia.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "docente")
data class DocenteEntity(
    @PrimaryKey
    @ColumnInfo(name = "matricula_docente") val matriculaDocente: String,
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "apellido_p") val apellidoP: String,
    @ColumnInfo(name = "apellido_m") val apellidoM: String?
)