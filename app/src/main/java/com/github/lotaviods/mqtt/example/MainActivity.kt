package com.github.lotaviods.mqtt.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.lotaviods.mqtt.example.databinding.ActivityMainBinding
import com.github.lotaviods.mqtt.example.mqtt.MqttManager
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val mqttManager: MqttManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)


        binding.btnSub.setOnClickListener {
            val text = binding.txtInput.text.toString()

            mqttManager.subscribe(arrayOf(text), intArrayOf(1))
        }


    }
}