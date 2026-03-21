package com.example.myapplicationbyyoshi

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.TypedValue
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * Actividad principal de la aplicación "Llego el picudazo".
 * Muestra un texto animado con un fondo de galaxia en movimiento (efecto parallax).
 */
class MainActivity : AppCompatActivity() {

    // --- VISTAS ---
    // Declaramos las variables para las vistas de forma diferida (lateinit),
    // ya que se inicializarán en onCreate.
    private lateinit var capaFondoLejano: ImageView
    private lateinit var capaFondoMedio: ImageView
    private lateinit var capaFondoCercano: ImageView
    private lateinit var textoPicudazo: TextView

    // --- ANIMACIONES ---
    // Lista para almacenar los animadores del efecto parallax y poder controlarlos
    // (pausar/reanudar) junto con el ciclo de vida de la actividad.
    private val listaDeAnimadores = mutableListOf<ValueAnimator>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Vinculamos las vistas del layout (XML) con nuestras variables en Kotlin.
        inicializarVistas()

        // 2. Iniciamos todas las animaciones.
        iniciarAnimaciones()
    }

    /**
     * Vincula las variables de la clase con las vistas definidas en el archivo XML 'activity_main.xml'.
     */
    private fun inicializarVistas() {
        capaFondoLejano = findViewById(R.id.layerFar)
        capaFondoMedio = findViewById(R.id.layerMid)
        capaFondoCercano = findViewById(R.id.layerNear)
        textoPicudazo = findViewById(R.id.tvPicudazo)
    }

    /**
     * Carga y comienza las animaciones para el texto y el fondo.
     */
    private fun iniciarAnimaciones() {
        // Animación de "pulso" para el texto, cargada desde un archivo XML en res/anim.
        val animacionPulso = AnimationUtils.loadAnimation(this, R.anim.pulse)
        textoPicudazo.startAnimation(animacionPulso)

        // Animaciones de parallax para las capas del fondo.
        // Cada capa se mueve a una velocidad y amplitud diferente para crear una ilusión de profundidad.
        // Las agregamos a nuestra lista para poder controlarlas después.
        listaDeAnimadores += crearAnimacionParallax(capaFondoLejano, convertirDpAPixeles(12f), 28000L) // Más lento y sutil
        listaDeAnimadores += crearAnimacionParallax(capaFondoMedio, convertirDpAPixeles(18f), 22000L)
        listaDeAnimadores += crearAnimacionParallax(capaFondoCercano, convertirDpAPixeles(26f), 16000L) // Más rápido y notorio
    }

    /**
     * Crea y configura un ObjectAnimator para mover una vista horizontalmente (translationX),
     * creando un efecto de parallax suave que va y viene.
     *
     * @param vista La ImageView que se va a animar.
     * @param amplitudEnPixeles La distancia máxima (en píxeles) que la vista se moverá desde su centro.
     * @param duracionEnMilisegundos La duración de un ciclo completo de la animación (ida y vuelta).
     * @return El ValueAnimator creado y ya iniciado.
     */
    private fun crearAnimacionParallax(vista: ImageView, amplitudEnPixeles: Float, duracionEnMilisegundos: Long): ValueAnimator {
        // Usamos un ObjectAnimator que modifica la propiedad "translationX" de la vista.
        val animador = ObjectAnimator.ofPropertyValuesHolder(
            vista,
            PropertyValuesHolder.ofFloat("translationX", -amplitudEnPixeles, amplitudEnPixeles)
        ).apply {
            duration = duracionEnMilisegundos // Duración del trayecto de un extremo a otro
            repeatCount = ValueAnimator.INFINITE // Repetir infinitamente
            repeatMode = ValueAnimator.REVERSE // Al llegar al final, vuelve en reversa
            start() // Inicia la animación inmediatamente
        }
        return animador
    }

    /**
     * Convierte un valor en DP (Density-independent Pixels) a Píxeles (px),
     * asegurando que la animación se vea consistente en pantallas con diferentes densidades.
     *
     * @param valorEnDp El valor en DP que se quiere convertir.
     * @return El valor equivalente en píxeles.
     */
    private fun convertirDpAPixeles(valorEnDp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            valorEnDp,
            resources.displayMetrics
        )
    }

    // --- GESTIÓN DEL CICLO DE VIDA DE LA ACTIVIDAD ---
    // Es una buena práctica pausar las animaciones cuando la app no está visible
    // para ahorrar batería y recursos del sistema.

    override fun onPause() {
        super.onPause()
        // Pausamos todas las animaciones de la lista cuando la actividad se pausa.
        listaDeAnimadores.forEach { it.pause() }
    }



    override fun onResume() {
        super.onResume()
        // Reanudamos todas las animaciones cuando la actividad vuelve a ser visible.
        listaDeAnimadores.forEach { it.resume() }
    }
}