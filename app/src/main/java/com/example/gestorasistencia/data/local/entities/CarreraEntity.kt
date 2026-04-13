package com.example.gestorasistencia.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "carrera",
    indices = [Index(value = ["nombre"], unique = true)]
)
data class CarreraEntity(
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "carrera_id") val carreraId: Int = 0,
    @ColumnInfo(name = "nombre") val nombre: String
)