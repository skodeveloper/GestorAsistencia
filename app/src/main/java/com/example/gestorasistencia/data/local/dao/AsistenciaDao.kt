package com.example.gestorasistencia.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gestorasistencia.data.local.entities.AsistenciaEntity

@Dao
interface AsistenciaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun registrarAsistencia(asistencia: AsistenciaEntity)

    // ¡La joya de la corona! Saca todas las asistencias que NO se han subido a la nube
    @Query("SELECT * FROM asistencia WHERE sincronizado = 0")
    suspend fun getAsistenciasPendientesDeSincronizar(): List<AsistenciaEntity>

    // Actualiza el estado a sincronizado una vez que vuelva el internet
    @Query("UPDATE asistencia SET sincronizado = 1 WHERE asistencia_id = :id")
    suspend fun marcarComoSincronizada(id: Int)
}