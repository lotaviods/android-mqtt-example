package com.github.lotaviods.mqtt.example.di.modules

import com.github.lotaviods.mqtt.example.mqtt.MqttManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

class MqttDi {
    companion object {
        val module = module {
            single {
                MqttManager(androidContext())
            }
        }
    }
}