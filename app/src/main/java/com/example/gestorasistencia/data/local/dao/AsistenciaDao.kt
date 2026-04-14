package com.example.gestorasistencia.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import com.example.gestorasistencia.data.local.entities.AsistenciaEntity

@Dao
interface AsistenciaDao {
    
    /**
     * Inserta un nuevo registro. Si por alguna razón de hilo concurrente
     * hay un conflicto de IDs, la estrategia REPLACE garantiza que no haya un crash.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarAsistencia(asistencia: AsistenciaEntity)

    /**
     * Obtiene exclusivamente los registros que ocurrieron sin internet
     * y que están esperando ser subidos al servidor.
     */
    @Query("SELECT * FROM asistencia WHERE sincronizado = 0")
    suspend fun obtenerAsistenciasPendientes(): List<AsistenciaEntity>

    /**
     * Devuelve un flujo reactivo (Flow) con todo el historial, ideal para 
     * mostrar una lista en tiempo real en la UI usando Jetpack Compose.
     */
    @Query("SELECT * FROM asistencia ORDER BY fecha DESC, hora DESC")
    fun obtenerTodasLasAsistenciasFlow(): Flow<List<AsistenciaEntity>>

    /**
     * Actualiza el estado de un registro (comúnmente usado para cambiar 'sincronizado' a true).
     */
    @Update
    suspend fun actualizarAsistencia(asistencia: AsistenciaEntity)
    
    /**
     * Operación de mantenimiento: borra del teléfono los registros que 
     * ya están respaldados en la nube para ahorrar espacio de almacenamiento.
     */
    @Query("DELETE FROM asistencia WHERE sincronizado = 1")
    suspend fun limpiarAsistenciasSincronizadas()

    //  --  Registro a base de matricula, día y hora    --  //
    @Query("""
        INSERT INTO asistencia (universal_id, matricula_id, opcion_menu, clase_id)
        VALUES (:uuid, :matricula, :opcion, (
            SELECT c.clase_id 
            FROM clase c
            JOIN horario h ON c.clase_id = h.clase_id
            JOIN carga_academica ca ON c.clase_id = ca.clase_id
            WHERE ca.matricula_id = :matricula
              AND h.dia_semana = :diaNombre
              AND time('now', 'localtime') BETWEEN h.hora_inicio AND h.hora_fin
            LIMIT 1
        ))
    """)
    suspend fun registrarAsistenciaAutomatica(uuid: String, matricula: String, opcion: String, diaNombre: String)

    //  --  Asistencias de hoy  --  //
    @Query("SELECT * FROM asistencia WHERE fecha = date('now', 'localtime')")
    suspend fun obtenerAsistenciasDeHoy(): List<AsistenciaEntity>
}