package com.example.gestorasistencia.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ciclo_escolar")
data class CicloEscolarEntity(
    @ColumnInfo(name = "ciclo_id") val cicloId: String,
    @ColumnInfo(name = "fecha_inicio") val cicloId: String,
)