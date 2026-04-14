package com.example.gestorasistencia.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ciclo_escolar")
data class CicloEscolarEntity(
    @PrimaryKey
    @ColumnInfo(name = "ciclo_id") val cicloId: String,
    @ColumnInfo(name = "fecha_inicio") val fechaInicio: String,
    @ColumnInfo(name = "fecha_fin") val fechaFin: String,
    @ColumnInfo(name = "estatus") val estatus: Int = 1
)