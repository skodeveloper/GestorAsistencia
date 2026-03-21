pluginManagement {
    repositories {
        // Hemos eliminado el bloque 'content' con filtros.
        // Ahora Gradle buscará en todos los repositorios sin restricciones.
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "My Application by Yoshi"
include(":app")
