package com.example.gestorasistencia.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carga_academica")
data class CargaAcademicaEntity(
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "carga_id") val cargaId: Int = 0,
    @ColumnInfo(name = "matricula_id") val matriculaId: String,
    @ColumnInfo(name = "clase_id") val claseId: Int,
    @ColumnInfo(name = "ciclo_id") val cicloId: String,
    @ColumnInfo(name = "sincronizado") val sincronizado: Int = 0
)