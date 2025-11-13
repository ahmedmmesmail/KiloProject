package com.amme.al_boslahda3et

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amme.al_boslahda3et.ui.theme.AlBoslahDa3etTheme
import kotlin.math.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlBoslahDa3etTheme {
                Scaffold { innerPadding ->
                    CompassScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CompassScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val sensorManager = remember { context.getSystemService(SensorManager::class.java) }

    var sensorAzimuthRaw by remember { mutableStateOf(0f) }
    var sensorAzimuthFiltered by remember { mutableStateOf(0f) }

    val alpha = 0.05f // لتقليل الاهتزاز

    DisposableEffect(sensorManager) {
        val accel = FloatArray(3)
        val magnet = FloatArray(3)
        val rotationMatrix = FloatArray(9)
        val orientationValues = FloatArray(3)

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event == null) return
                when (event.sensor.type) {
                    Sensor.TYPE_ACCELEROMETER -> event.values.copyInto(accel)
                    Sensor.TYPE_MAGNETIC_FIELD -> event.values.copyInto(magnet)
                }

                val success = SensorManager.getRotationMatrix(rotationMatrix, null, accel, magnet)
                if (success) {
                    SensorManager.getOrientation(rotationMatrix, orientationValues)
                    var angle = Math.toDegrees(orientationValues[0].toDouble()).toFloat()
                    angle = (angle % 360f + 360f) % 360f
                    sensorAzimuthRaw = angle

                    val diff = ((sensorAzimuthRaw - sensorAzimuthFiltered + 540f) % 360f) - 180f
                    sensorAzimuthFiltered = (sensorAzimuthFiltered + alpha * diff + 360f) % 360f
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        val accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        if (accelSensor != null && magSensor != null) {
            sensorManager.registerListener(listener, accelSensor, SensorManager.SENSOR_DELAY_UI)
            sensorManager.registerListener(listener, magSensor, SensorManager.SENSOR_DELAY_UI)
        }

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    val direction = remember(sensorAzimuthFiltered) {
        when (sensorAzimuthFiltered.roundToInt()) {
            in 350..360, in 0..10 -> "N"
            in 11..80 -> "NE"
            in 81..100 -> "E"
            in 101..170 -> "SE"
            in 171..190 -> "S"
            in 191..260 -> "SW"
            in 261..280 -> "W"
            in 281..349 -> "NW"
            else -> ""
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // الدرجة والاتجاه فوق البوصلة
        Text(
            text = "${sensorAzimuthFiltered.roundToInt()}°",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF03DAC5)
        )
        Text(
            text = direction,
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF03DAC5)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(250.dp)
        ) {
            // صورة البوصلة الثابتة
            Image(
                painter = painterResource(id = R.drawable.compass),
                contentDescription = "Compass",
                modifier = Modifier.fillMaxSize()
            )

            // سهم يشير للاتجاه
            Image(
                painter = painterResource(id = R.drawable.needle),
                contentDescription = "Needle",
                modifier = Modifier
                    .size(100.dp)
                    .offset(y = -32.dp)
                    .graphicsLayer {
                        rotationZ = sensorAzimuthFiltered
                        transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0.5f, 0.8f)
                    }
            )
        }
    }
}
