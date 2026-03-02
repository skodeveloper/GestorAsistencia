package com.example.gestorasistencia.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gestorasistencia.data.local.entities.EstudianteEntity

@Dao
interface EstudianteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEstudiante(estudiante: EstudianteEntity)

    // Esta función la usaremos cuando el alumno escanee su QR para ver si existe
    @Query("SELECT * FROM estudiante WHERE matricula_id = :matriculaId")
    suspend fun getEstudiantePorMatricula(matriculaId: String): EstudianteEntity?

    // Para la descarga masiva a las 5 AM que pedía Lenin
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllEstudiantes(estudiantes: List<EstudianteEntity>)
}