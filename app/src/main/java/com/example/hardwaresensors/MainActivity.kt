package com.example.hardwaresensors

import android.graphics.Color
import android.graphics.ColorSpace
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.getSystemService
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.UnsupportedOperationException
import kotlin.math.roundToInt
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    lateinit var sensorEventListener:SensorEventListener
    lateinit var sensorManager: SensorManager
    lateinit var proximitySensor: Sensor
    private lateinit var accelSensor: Sensor

    val colors = arrayOf(Color.RED, Color.BLUE, Color.CYAN, Color.MAGENTA, Color.YELLOW)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService<SensorManager>()!!

        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        sensorEventListener = object :SensorEventListener{
            override fun onSensorChanged(event: SensorEvent?) {
                if(event?.sensor?.type == Sensor.TYPE_PROXIMITY){
                    if(event!!.values[0] > 0){
                        flProximityIndicator.setBackgroundColor(colors[Random.nextInt(5)])
                    }
                    Log.d("SNSCHANGE", """
                        onSensorChange: ${event!!.values[0]}
                        """.trimIndent())
                }
                if(event?.sensor?.type == Sensor.TYPE_ACCELEROMETER){
                    flAccelerometerIndicator.setBackgroundColor(Color.rgb(accellToColor(event!!.values[0]), accellToColor(event!!.values[1]), accellToColor(event!!.values[2])))
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

            }
        }

    }


    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
                sensorEventListener,
                accelSensor,
                1000 * 1000
        )

    }

    override fun onPause() {
        sensorManager.unregisterListener(sensorEventListener)
        super.onPause()
    }

    private fun accellToColor(accell:Float) = (((accell + 12) / 24) * 255).roundToInt()

}