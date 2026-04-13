package com.example.gestorasistencia.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gestorasistencia.data.local.dao.AsistenciaDao
import com.example.gestorasistencia.data.local.entities.AsistenciaEntity

@Database(
    entities = [AsistenciaEntity::class], 
    version = 1, 
    exportSchema = false // Desactivado temporalmente para agilizar la compilación en fase de prototipado
)
abstract class GestorDatabase : RoomDatabase() {
    // Exposición del DAO para el Repositorio
    abstract fun asistenciaDao(): AsistenciaDao

    companion object {
        // @Volatile asegura que los cambios en INSTANCE sean visibles inmediatamente en todos los hilos
        @Volatile
        private var INSTANCE: GestorDatabase? = null

        fun getDatabase(context: Context): GestorDatabase {
            // Patrón Singleton con doble comprobación de bloqueo para concurrencia segura
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GestorDatabase::class.java,
                    "GestorAsistencia.db"
                )
                    .createFromAsset("databases/GestorAsistencia.db")
                // Destruye y recrea la DB si se cambia la versión (ideal para desarrollo temprano)
                .fallbackToDestructiveMigration()
                .build()

                INSTANCE = instance
                instance
            }
        }
    }
}