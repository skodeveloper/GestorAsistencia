package com.example.gestorasistencia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.gestorasistencia.ui.theme.GestorAsistenciaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GestorAsistenciaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SaludoAsistencia(
                        name = "Equipo",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun SaludoAsistencia(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "¡Listo $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun SaludoAsistenciaPreview() {
    GestorAsistenciaTheme {
        SaludoAsistencia("Equipo")
    }
}