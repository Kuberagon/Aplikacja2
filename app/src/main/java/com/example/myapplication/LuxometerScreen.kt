package com.example.myapplication.ui

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext

@Composable
fun LuxometerScreen(navController: NavController) {
    val context = LocalContext.current
    val lightSensorManager = remember { LightSensorManager(context) }

    LaunchedEffect(Unit) { lightSensorManager.startListening() }
    DisposableEffect(Unit) { onDispose { lightSensorManager.stopListening() } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = lightSensorManager.lightValue.value,
            fontSize = 24.sp,
            modifier = Modifier.padding(8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("gyroscope") }) {
            Text(text = "Przejdź do Żyroskopu")
        }
    }
}

class LightSensorManager(context: Context) : SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val lightSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

    val lightValue = mutableStateOf("Światło: -- lx")

    fun startListening() {
        lightSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return
        lightValue.value = "Światło: ${event.values[0]} lx"
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
