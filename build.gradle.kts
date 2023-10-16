// Definición del bloque buildscript
buildscript {
    // Definición de los repositorios
    repositories {
        google()
        mavenCentral()
    }

    // Definición de las dependencias
    dependencies {
        // Adición de la dependencia para el complemento de servicios de Google Gradle
        classpath("com.google.gms:google-services:4.3.14")
    }
}

// Definición del bloque plugins
plugins {
    // Aplicación del complemento com.android.application
    id("com.android.application") version "7.4.2" apply false

    // Aplicación del complemento com.android.library
    id("com.android.library") version "7.4.2" apply false
}