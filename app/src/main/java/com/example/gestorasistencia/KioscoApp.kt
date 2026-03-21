package com.example.gestorasistencia

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// --- COLORES DEL DISEÑO ORIGINAL HTML ---
val BackgroundDark = Color(0xFF05050A)
val AccentCyan = Color(0xFF38BDF8)
val AccentPink = Color(0xFFF43F5E)
val TextMain = Color(0xFFFFFFFF)
val TextMuted = Color(0xFFCBD5E1)

// --- ESTADOS DE LA PANTALLA ---
enum class ScreenState {
    IDLE, ENTRADA, SALIDA, DOBLE_ESCANEO, ADMIN
}

// --- MODIFICADOR PERSONALIZADO PARA EFECTO CRISTAL (GLASSMORPHISM) ---
fun Modifier.glassmorphism(): Modifier = this
    .clip(RoundedCornerShape(32.dp))
    .background(Color.White.copy(alpha = 0.05f))
    .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(32.dp))

@Composable
fun KioscoAppScreen() {
    var currentState by remember { mutableStateOf(ScreenState.IDLE) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        // 1. EL FONDO ANIMADO (Las manchas borrosas del HTML)
        AnimatedAmbientBackground()

        // 2. NAVEGACIÓN DE PANTALLAS
        AnimatedContent(
            targetState = currentState,
            label = "ScreenTransition",
            modifier = Modifier.fillMaxSize()
        ) { state ->
            when (state) {
                ScreenState.IDLE -> IdleScreen(
                    onSimularEntrada = { currentState = ScreenState.ENTRADA }
                )
                ScreenState.ENTRADA -> EntradaScreen(
                    onVolver = { currentState = ScreenState.IDLE }
                )
                ScreenState.SALIDA -> SalidaScreen(
                    onVolver = { currentState = ScreenState.IDLE }
                )
                ScreenState.DOBLE_ESCANEO -> DobleEscaneoScreen(
                    onRegistrarSalida = { currentState = ScreenState.SALIDA },
                    onVerInfo = { /* TODO: Handle Ver Info */ },
                    onVolver = { currentState = ScreenState.IDLE }
                )
                ScreenState.ADMIN -> AdminScreen(
                    onVolver = { currentState = ScreenState.IDLE }
                )
            }
        }
    }
}

// --- FONDO ANIMADO ---
@Composable
fun AnimatedAmbientBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "blob_animation")
    val offsetAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "blob_movement"
    )

    Canvas(modifier = Modifier
        .fillMaxSize()
        .blur(100.dp)) {
        // Mancha Morada
        drawCircle(
            color = Color(0xFF8A4D91).copy(alpha = 0.5f),
            radius = 600f,
            center = Offset(0f + offsetAnimation, 0f + offsetAnimation)
        )
        // Mancha Rosa
        drawCircle(
            color = Color(0xFFFB8FF0).copy(alpha = 0.4f),
            radius = 800f,
            center = Offset(size.width - offsetAnimation, size.height + offsetAnimation)
        )
        // Mancha Azul/Cyan
        drawCircle(
            color = Color(0xFF38BDF8).copy(alpha = 0.3f),
            radius = 500f,
            center = Offset(size.width / 2, size.height / 2 - offsetAnimation)
        )
    }
}

// --- PANTALLA 1: ESPERA (IDLE) ---
@Composable
fun IdleScreen(onSimularEntrada: () -> Unit) {
    // Reloj en tiempo real
    var timeText by remember { mutableStateOf("") }
    var dateText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        while (true) {
            timeText = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            dateText = SimpleDateFormat("EEEE, d 'de' MMMM", Locale("es", "ES")).format(Date())
                .replaceFirstChar { it.uppercase() }
            delay(1000)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Hora superior izquierda
        Column(modifier = Modifier
            .padding(40.dp)
            .align(Alignment.TopStart)) {
            Text(text = timeText, fontSize = 80.sp, fontWeight = FontWeight.Bold, color = TextMain)
            Text(text = dateText, fontSize = 24.sp, color = TextMuted)
        }

        // Píldora de estado (Guardado Local)
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(40.dp)
                .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(30.dp))
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.CloudDownload, contentDescription = null, tint = AccentCyan)
            Spacer(modifier = Modifier.width(10.dp))
            Text("Guardado Local Activo", color = TextMain)
        }

        // Mensaje central parpadeante
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Acerque su gafete al lector QR",
                fontSize = 40.sp,
                fontWeight = FontWeight.Medium,
                color = TextMain
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "El registro de asistencia se procesará automáticamente",
                fontSize = 18.sp,
                color = TextMuted
            )
        }

        // Botón de prueba temporal (Para simular que se escaneó un código)
        Button(
            onClick = onSimularEntrada,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(30.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentCyan)
        ) {
            Text("Simular Escaneo QR (Entrada)", color = BackgroundDark)
        }
    }
}

// --- PANTALLA 2: ENTRADA EXITOSA ---
@Composable
fun EntradaScreen(onVolver: () -> Unit) {
    // Regresar automáticamente después de 5 segundos (Timeout)
    LaunchedEffect(Unit) {
        delay(5000)
        onVolver()
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        // Tarjeta Glassmorphism
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .glassmorphism()
                .padding(50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Píldora de Acceso
            Row(
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(30.dp))
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = AccentCyan)
                Spacer(modifier = Modifier.width(10.dp))
                Text("ACCESO DE ENTRADA REGISTRADO", color = AccentCyan, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Datos del Alumno simulados
            Text(
                text = "Cristian Ramces",
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold,
                color = TextMain,
                textAlign = TextAlign.Center,
                lineHeight = 65.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Matrícula: 20261014",
                fontSize = 24.sp,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Caja de Clase
            Column(
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                    .padding(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Clase Actual", color = TextMuted, fontSize = 18.sp)
                Text("Ingeniería de Software II", color = TextMain, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// --- PANTALLA 3: SALIDA (NUEVA) ---
@Composable
fun SalidaScreen(onVolver: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(5000)
        onVolver()
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .glassmorphism()
                .padding(50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(30.dp))
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = AccentPink)
                Spacer(modifier = Modifier.width(10.dp))
                Text("SALIDA REGISTRADA", color = AccentPink, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "Hasta pronto",
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold,
                color = TextMain,
                textAlign = TextAlign.Center
            )
        }
    }
}

// --- PANTALLA 4: DOBLE ESCANEO (NUEVA) ---
@Composable
fun DobleEscaneoScreen(onRegistrarSalida: () -> Unit, onVerInfo: () -> Unit, onVolver: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .glassmorphism()
                .padding(50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Ya tienes una entrada registrada",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = TextMain,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text("¿Qué deseas hacer?", fontSize = 24.sp, color = TextMuted)
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = onRegistrarSalida,
                    colors = ButtonDefaults.buttonColors(containerColor = AccentPink),
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                ) {
                    Text("Registrar Salida", fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.width(20.dp))
                OutlinedButton(
                    onClick = onVerInfo,
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                ) {
                    Text("Ver Información", color = TextMain, fontSize = 18.sp)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            TextButton(onClick = onVolver) {
                Text("Cancelar", color = TextMuted)
            }
        }
    }
}


// --- PANTALLA 5: ADMIN (NUEVA) ---
@Composable
fun AdminScreen(onVolver: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .width(400.dp)
                .glassmorphism()
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Filled.Lock,
                contentDescription = "Admin Login",
                tint = TextMuted,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text("Acceso de Administrador", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextMain)
            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextMain,
                    unfocusedTextColor = TextMain,
                    cursorColor = AccentCyan,
                    focusedContainerColor = Color.Black.copy(alpha = 0.3f),
                    unfocusedContainerColor = Color.Black.copy(alpha = 0.3f),
                    focusedBorderColor = AccentCyan,
                    unfocusedBorderColor = TextMuted
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextMain,
                    unfocusedTextColor = TextMain,
                    cursorColor = AccentCyan,
                    focusedContainerColor = Color.Black.copy(alpha = 0.3f),
                    unfocusedContainerColor = Color.Black.copy(alpha = 0.3f),
                    focusedBorderColor = AccentCyan,
                    unfocusedBorderColor = TextMuted
                )
            )
            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = { /* TODO: Login Logic */ onVolver() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentCyan)
            ) {
                Text("Login", color = BackgroundDark, fontWeight = FontWeight.Bold)
            }
            TextButton(onClick = onVolver) {
                Text("Volver", color = TextMuted)
            }
        }
    }
}


// --- VISTA PREVIA PARA ANDROID STUDIO ---
@Preview(showBackground = true, widthDp = 1280, heightDp = 800)
@Composable
fun PreviewKioscoApp() {
    MaterialTheme {
        KioscoAppScreen()
    }
}
