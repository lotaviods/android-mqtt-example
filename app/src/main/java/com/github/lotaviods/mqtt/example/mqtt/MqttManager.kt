package com.github.lotaviods.mqtt.example.mqtt

import android.content.Context
import android.util.Log
import com.github.lotaviods.mqtt.example.BuildConfig
import info.mqtt.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.util.UUID

class MqttManager(context: Context) {

    private val client = MqttAndroidClient(context, MQTT_URL_BASE, UUID.randomUUID().toString())


    init {
        client.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(b: Boolean, s: String) {
                Log.i(TAG_STATUS, "connected $s, reconnected: $b")
            }

            override fun connectionLost(throwable: Throwable?) {
                Log.i(TAG_STATUS, "connectionLost", throwable)
            }

            override fun messageArrived(topic: String?, mqttMessage: MqttMessage?) {
                topic?.split("/")?.let {
                    Log.d(TAG_MSG, "$topic ${mqttMessage.toString()}")
                }
            }

            override fun deliveryComplete(iMqttDeliveryToken: IMqttDeliveryToken?) {
            }
        })

        connect()
    }

    private fun connect() {
        Log.i(TAG_STATUS, "connecting...")

        val mqttConnectOptions = MqttConnectOptions().apply {
            isAutomaticReconnect = true
            isCleanSession = false
            userName = USERNAME
            password = PASSWORD.toCharArray()
        }

        client.connect(mqttConnectOptions, null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                val disconnectedBufferOptions = DisconnectedBufferOptions().apply {
                    isBufferEnabled = true
                    bufferSize = 100
                    isPersistBuffer = false
                    isDeleteOldestMessages = false
                }
                try {
                    client.setBufferOpts(disconnectedBufferOptions)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.e(TAG_STATUS, "not connected", exception)
            }
        })
    }

    fun subscribe(topics: Array<String>, qos: IntArray) {
        try {
            if (client.isConnected) {
                client.subscribe(topics, qos, null, object : IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken?) {
                        Log.i(TAG_STATUS, "subscribe successfully")
                    }

                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                        Log.e(TAG_STATUS, "subscribe fail")
                    }

                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val MQTT_URL_BASE: String = BuildConfig.MQTT_URL_BASE
        private const val USERNAME: String = BuildConfig.MQTT_USER
        private const val PASSWORD: String = BuildConfig.MQTT_PASSWORD
        private const val TAG_STATUS = "status MQTT"
        private const val TAG_MSG = "messageArrived MQTT"
    }

}