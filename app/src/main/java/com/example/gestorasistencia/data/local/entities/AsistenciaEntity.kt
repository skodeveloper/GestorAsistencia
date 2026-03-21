package com.example.gestorasistencia.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Representa un registro de asistencia en la base de datos local (Room).
 * Esta entidad está diseñada específicamente para soportar el modo Offline-First.
 */
@Entity(tableName = "asistencias_table")
data class AsistenciaEntity(
    // ID local autogenerado para manejo interno de SQLite
    @PrimaryKey(autoGenerate = true) 
    @ColumnInfo(name = "id_local") val id: Int = 0,
    
    // UUID universal para evitar colisiones al sincronizar con el servidor (Ej. MySQL/Firebase)
    @ColumnInfo(name = "universal_id") val universalId: String = UUID.randomUUID().toString(),
    
    // Matrícula o identificador único del estudiante escaneado
    @ColumnInfo(name = "alumno_matricula") val alumnoId: Int,
    
    // Timestamp exacto del momento en que el QR fue escaneado (en milisegundos)
    @ColumnInfo(name = "fecha_registro") val fecha: Long = System.currentTimeMillis(),
    
    // Bandera fundamental para el WorkManager: indica si este registro ya se subió a la nube
    @ColumnInfo(name = "esta_sincronizado") val sincronizado: Boolean = false
)