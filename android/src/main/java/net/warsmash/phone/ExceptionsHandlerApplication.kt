package net.warsmash.phone

import android.app.Application

class ExceptionsHandlerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler(CustomExceptionHandler())
    }
}