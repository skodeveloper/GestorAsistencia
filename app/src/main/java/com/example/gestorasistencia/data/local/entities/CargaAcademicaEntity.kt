package com.example.gestorasistencia.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carga_academica")
data class CargaAcademicaEntity(
    @PrimaryKey(autoGenerate = true)
    val carga_id: Int = 0,
    val matricula_id: String,
    val clase_id: Int,
    val ciclo_id: String,
    val sincronizado: Boolean // 1 Sincronizado, 0 Pendiente
)
