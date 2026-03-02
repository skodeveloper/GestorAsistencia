# 📱 Gestor de Asistencia - Android Client

![Kotlin](https://img.shields.io/badge/Kotlin-B125EA?style=for-the-badge&logo=kotlin&logoColor=white)
![Android Studio](https://img.shields.io/badge/Android%20Studio-3DDC84.svg?style=for-the-badge&logo=android-studio&logoColor=white)
![Room Database](https://img.shields.io/badge/Room_SQLite-005C84?style=for-the-badge&logo=sqlite&logoColor=white)

Aplicación cliente para el sistema de Gestión de Asistencias, desarrollada de forma nativa para Android. Este proyecto está diseñado con un enfoque **Offline-First**, garantizando que el registro de asistencias funcione incluso sin conexión a internet, sincronizando los datos posteriormente con el servidor central.

## 🏗️ Arquitectura del Proyecto

El proyecto sigue los lineamientos de **Clean Architecture** y el patrón de diseño **MVVM (Model-View-ViewModel)** para asegurar la escalabilidad, el mantenimiento y la separación de responsabilidades.

La capa de datos (Data Layer) ya se encuentra implementada con:
* **Room Database:** Motor de persistencia local.
* **Entidades Relacionales:** Modelado de datos basado en el diccionario del proyecto (Estudiante, Asistencia, Carga Académica).
* **DAOs Asíncronos:** Consultas preparadas para Corrutinas, incluyendo el filtrado de registros pendientes de sincronización (Modo Offline).

## 🛠️ Stack Tecnológico

* **Lenguaje:** Kotlin
* **UI Toolkit:** Jetpack Compose (Material Design 3)
* **Base de Datos Local:** Room (KSP)
* **Programación Asíncrona:** Kotlin Coroutines & Flow

## 🚀 Próximos Pasos (En desarrollo)
- [ ] Integración de la capa de Interfaz de Usuario (UI/Compose).
- [ ] Implementación del Patrón Repository.
- [ ] Configuración de Retrofit para consumo de API REST.
- [ ] Lógica de WorkManager para sincronización en segundo plano.

## 💻 Instrucciones para el equipo (Cómo levantar el proyecto)

1. Clona este repositorio en tu máquina local:
   ```bash
   git clone [https://github.com/skodeveloper/GestorAsistencia.git](https://github.com/skodeveloper/GestorAsistencia.git)
