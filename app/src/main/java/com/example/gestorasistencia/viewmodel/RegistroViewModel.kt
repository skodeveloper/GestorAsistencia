package com.example.gestorasistencia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestorasistencia.data.local.GestorDatabase
import com.example.gestorasistencia.data.local.entities.AsistenciaEntity
import com.example.gestorasistencia.data.repository.AsistenciaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class RegistroViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AsistenciaRepository

    // Usamos StateFlow porque es el estándar moderno para Jetpack Compose
    private val _uiState = MutableStateFlow<String>("Esperando código QR...")
    val uiState: StateFlow<String> = _uiState.asStateFlow()

    init {
        // Inicializamos correctamente sacando el DAO de la base de datos que ya armaste
        val dao = GestorDatabase.getDatabase(application).asistenciaDao()
        repository = AsistenciaRepository(dao)
    }

    // Función principal que procesa el QR y usa Corrutinas en lugar de Threads
    fun procesarQR(qrString: String?) {
        if (qrString.isNullOrBlank()) {
            _uiState.value = "Código QR inválido o vacío"
            return
        }

        val matriculaString = qrString.trim()
        _uiState.value = "Procesando: $matriculaString..."

        // Corrutina segura ejecutándose en hilo de fondo (IO)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Convertir la matrícula a Int. Si no es un número, no proceder.
                val matriculaInt = matriculaString.toIntOrNull()
                if (matriculaInt == null) {
                    _uiState.value = "Error: La matrícula no es un número válido."
                    return@launch
                }

                val nuevaAsistencia = AsistenciaEntity(
                    // id es autogenerado
                    // universalId se genera automáticamente en el constructor
                    // alumnoId = matriculaInt,
                    // fecha se genera automáticamente
                    // sincronizado es false por defecto
                    universalId = UUID.randomUUID().toString(),
                    matriculaId = matriculaString,
                    opcionMenu = "Entrada",
                    claseId = 0,
                    sincronizado = 0
                )
                
                repository.registrarAsistencia(nuevaAsistencia)
                
                // Actualizamos la UI en Compose
                _uiState.value = "Asistencia guardada exitosamente"
                
            } catch (e: NumberFormatException) {
                 _uiState.value = "Error: La matrícula no es un número válido."
            } catch (e: Exception) {
                _uiState.value = "Error al guardar en base de datos: ${e.message}"
            }
        }
    }
}