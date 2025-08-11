package com.tbart.pushup

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // Ici tu peux initialiser des choses globales si besoin
        // Par exemple : Logger, configuration de Room si en singleton
    }
}
