package com.example.gestorasistencia.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gestorasistencia.data.local.dao.AsistenciaDao
import com.example.gestorasistencia.data.local.dao.CargaAcademicaDao
import com.example.gestorasistencia.data.local.dao.EstudianteDao
import com.example.gestorasistencia.data.local.entities.AsistenciaEntity
import com.example.gestorasistencia.data.local.entities.CargaAcademicaEntity
import com.example.gestorasistencia.data.local.entities.EstudianteEntity

// Aquí le decimos a Room cuáles son las tablas de la base de datos
@Database(
    entities = [
        EstudianteEntity::class, 
        AsistenciaEntity::class, 
        CargaAcademicaEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class GestorDatabase : RoomDatabase() {

    // Aquí le damos acceso a las consultas (DAOs)
    abstract fun estudianteDao(): EstudianteDao
    abstract fun asistenciaDao(): AsistenciaDao
    abstract fun cargaAcademicaDao(): CargaAcademicaDao

    // Patrón Singleton: Asegura que solo exista UNA instancia de la base de datos abierta a la vez para evitar colapsos
    companion object {
        @Volatile
        private var INSTANCE: GestorDatabase? = null

        fun getDatabase(context: Context): GestorDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GestorDatabase::class.java,
                    "gestor_asistencia_db"
                )
                .fallbackToDestructiveMigration() 
                .build()
                
                INSTANCE = instance
                instance
            }
        }
    }
}