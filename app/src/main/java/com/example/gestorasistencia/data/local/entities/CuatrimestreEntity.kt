package com.example.gestorasistencia.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cuatrimestre")
data class CuatrimestreEntity(
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "cuatrimestre_id") val cuatrimestreId: Int = 0,
    @ColumnInfo(name = "no_cuatrimestre") val noCuatrimestre: Int
)