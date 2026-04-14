package com.example.gestorasistencia.data.repository

import com.example.gestorasistencia.data.local.dao.AsistenciaDao
import com.example.gestorasistencia.data.local.entities.AsistenciaEntity

class AsistenciaRepository(private val asistenciaDao: AsistenciaDao) {

    /**
     * Registrar asistencia en la base de datos local
     */
    suspend fun registrarAsistencia(asistencia: AsistenciaEntity) {
        asistenciaDao.insertarAsistencia(asistencia)
    }

    /**
     * Obtener asistencias pendientes para cuando regrese el internet (Modo Offline)
     */
    suspend fun obtenerAsistenciasPendientes(): List<AsistenciaEntity> {
        return asistenciaDao.obtenerAsistenciasPendientes()
    }

    /**
     * Marcar asistencia como sincronizada
     */
    suspend fun marcarComoSincronizado(asistencia: AsistenciaEntity) {
        val asistenciaActualizada = asistencia.copy(sincronizado = 1) //    --  Corrección True -> 1    --  //
        asistenciaDao.actualizarAsistencia(asistenciaActualizada)
    }
}