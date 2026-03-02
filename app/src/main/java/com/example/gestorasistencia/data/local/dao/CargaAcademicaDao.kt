package com.example.gestorasistencia.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gestorasistencia.data.local.entities.CargaAcademicaEntity

@Dao
interface CargaAcademicaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCarga(carga: CargaAcademicaEntity)

    // Saber qué clases tiene el alumno hoy
    @Query("SELECT * FROM carga_academica WHERE matricula_id = :matriculaId")
    suspend fun getCargaPorAlumno(matriculaId: String): List<CargaAcademicaEntity>
}