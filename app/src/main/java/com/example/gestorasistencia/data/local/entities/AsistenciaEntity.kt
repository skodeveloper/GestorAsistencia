package com.example.gestorasistencia.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID
/**
 * Representa un registro de asistencia en la base de datos local (Room).
 * Esta entidad está diseñada específicamente para soportar el modo Offline-First.
 */
//--Corrección de nombre tabla--
@Entity(tableName = "asistencia")
data class AsistenciaEntity(
    // ID local autogenerado para manejo interno de SQLite
    @PrimaryKey(autoGenerate = true) 
    @ColumnInfo(name = "asistencia_id") val id: Int = 0,
    // UUID universal para evitar colisiones al sincronizar con el servidor (Ej. MySQL/Firebase)
    @ColumnInfo(name = "universal_id") val universalId: String = UUID.randomUUID().toString(),
    // Matrícula o identificador único del estudiante escaneado

    //--Cambio de nombre "alumno_matricula" "alumnoId: Int"--
    @ColumnInfo(name = "matricula_id") val matriculaId: String,
    // Timestamp exacto del momento en que el QR fue escaneado (en milisegundos)
    //--Cambio de nombre "fecha_registro" "fecha: Long = System.currentTimeMillis(),"--
    @ColumnInfo(name = "fecha") val fecha: String? = null,
    //  --  Nuevas columnas --  //
    @ColumnInfo(name = "hora") val hora: String? = null,
    @ColumnInfo(name = "opcion_menu") val opcionMenu: String,
    @ColumnInfo(name = "clase_id") val claseId: Int?,

    // Bandera fundamental para el WorkManager: indica si este registro ya se subió a la nube
    //  --  Cambio de nombre "esta_actulaizado" "boolean=false" --  //
    @ColumnInfo(name = "sincronizado") val sincronizado: Int = 0
)