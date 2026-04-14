package com.example.gestorasistencia.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clase")
data class ClaseEntity(
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "clase_id") val claseId: Int = 0,
    @ColumnInfo(name = "clave_materia") val claveMateria: String,
    @ColumnInfo(name = "matricula_docente") val matriculaDocente: String,
    @ColumnInfo(name = "cuatrimestre_id") val cuatrimestre: Int,
    @ColumnInfo(name = "ciclo_id") val cicloId: String
)