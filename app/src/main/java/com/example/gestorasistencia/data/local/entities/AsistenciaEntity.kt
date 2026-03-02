package com.example.gestorasistencia.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "asistencia")
data class AsistenciaEntity(
    @PrimaryKey(autoGenerate = true)
    val asistencia_id: Int = 0,
    val universal_id: String, // El famoso UUID para evitar duplicados
    val matricula_id: String,
    val fecha: String,
    val hora: String,
    val opcion_menu: String,
    val clase_id: Int,
    val sincronizado: Boolean // 1 Sincronizado, 0 Pendiente
)
