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
    @Query("SELECT * FROM asistencias_table WHERE esta_sincronizado = 0")
    suspend fun obtenerAsistenciasPendientes(): List<AsistenciaEntity>

    /**
     * Devuelve un flujo reactivo (Flow) con todo el historial, ideal para 
     * mostrar una lista en tiempo real en la UI usando Jetpack Compose.
     */
    @Query("SELECT * FROM asistencias_table ORDER BY fecha_registro DESC")
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
    @Query("DELETE FROM asistencias_table WHERE esta_sincronizado = 1")
    suspend fun limpiarAsistenciasSincronizadas()
}