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
fun GyroscopeScreen(navController: NavController) {
    val context = LocalContext.current
    val gyroscopeSensorManager = remember { GyroscopeSensorManager(context) }

    LaunchedEffect(Unit) { gyroscopeSensorManager.startListening() }
    DisposableEffect(Unit) { onDispose { gyroscopeSensorManager.stopListening() } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = gyroscopeSensorManager.gyroscopeValue.value,
            fontSize = 24.sp,
            modifier = Modifier.padding(8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("luxometer") }) {
            Text(text = "Przejdź do Luxometru")
        }
    }
}

class GyroscopeSensorManager(context: Context) : SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val gyroscopeSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

    val gyroscopeValue = mutableStateOf("Żyroskop:\nX: --\nY: --\nZ: --")

    fun startListening() {
        gyroscopeSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]
        gyroscopeValue.value = "Żyroskop:\nX: $x\nY: $y\nZ: $z"
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
