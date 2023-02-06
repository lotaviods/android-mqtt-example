package com.github.lotaviods.mqtt.example.app

import android.app.Application
import com.github.lotaviods.mqtt.example.di.modules.MqttDi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@Application)
            modules(
                listOf(
                    MqttDi.module
                )
            )
        }

    }

}