package com.example.gestorasistencia

import android.Manifest
import android.content.pm.PackageManager
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors

@Composable
fun PantallaLectorQR(onQrScanned: (String) -> Unit) {
    val context = LocalContext.current
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
        }
    )

    // Solicitar permiso al entrar si no se ha concedido
    LaunchedEffect(key1 = true) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (hasCameraPermission) {
            CameraPreview(onQrScanned)
        } else {
            PermissionRationale(onRequestPermission = {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            })
        }
    }
}

@Composable
private fun CameraPreview(onQrScanned: (String) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val haptic = LocalHapticFeedback.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
            val previewView = PreviewView(ctx).apply {
                this.scaleType = PreviewView.ScaleType.FILL_CENTER
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                // Configuración del Preview
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                // Configuración del Analizador de Imagen
                val imageAnalyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor, QrCodeAnalyzer(haptic, onQrScanned))
                    }

                // Selección de cámara
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    // Desvincular todo antes de volver a vincular
                    cameraProvider.unbindAll()
                    // Vincular casos de uso al ciclo de vida de la cámara
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner, cameraSelector, preview, imageAnalyzer
                    )
                } catch (exc: Exception) {
                    // Log para depuración
                }
            }, ContextCompat.getMainExecutor(ctx))
            previewView
        }
    )
}

@Composable
private fun PermissionRationale(onRequestPermission: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Se necesita acceso a la cámara para escanear los códigos QR de los gafetes.",
            textAlign = TextAlign.Center
        )
        Button(
            onClick = onRequestPermission,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Conceder Permiso")
        }
    }
}

private class QrCodeAnalyzer(
    private val hapticFeedback: androidx.compose.ui.hapticfeedback.HapticFeedback,
    private val onQrScanned: (String) -> Unit
) : ImageAnalysis.Analyzer {

    private var isScanning = true

    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        if (!isScanning) {
            imageProxy.close()
            return
        }

        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE) // Optimización clave
                .build()
            val scanner = BarcodeScanning.getClient(options)

            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isNotEmpty()) {
                        isScanning = false // Detener análisis para evitar bucles
                        barcodes.first()?.rawValue?.let { qrCodeValue ->
                            if (qrCodeValue.isNotBlank()) {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                onQrScanned(qrCodeValue)
                            } else {
                                isScanning = true // Reanudar si el QR está vacío
                            }
                        }
                    }
                }
                .addOnCompleteListener {
                    // Es crucial cerrar el ImageProxy en todos los casos
                    imageProxy.close()
                }
        }
    }
}
