package com.example.gestorasistencia.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "horario")
data class HorarioEntity(
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "horario_id") val horarioId: Int = 0,
    @ColumnInfo(name = "hora_inicio") val horaInicio: String,
    @ColumnInfo(name = "hora_fin") val horaFin: String,
    @ColumnInfo(name = "dia_semana") val diaSemana: String,
    @ColumnInfo(name = "clase_id") val claseId: Int
)