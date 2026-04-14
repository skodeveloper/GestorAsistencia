package com.example.gestorasistencia.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuario")
data class UsuarioEntity(
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "usuario_id") val usuarioId: Int = 0,
    @ColumnInfo(name = "nombre_usuario") val nombreUsuario: String,
    @ColumnInfo(name = "contrasena") val contrasena: String,
    @ColumnInfo(name = "rol") val rol: String
)