package com.example.gestorasistencia.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "materia")
data class MateriaEntity(
    @PrimaryKey
    @ColumnInfo(name = "clave_materia") val claveMateria: String,
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "carrera_id") val carreraId: Int,
    @ColumnInfo(name = "cuatrimestre_id") val cuatrimestreId: Int
)