package net.warsmash.phone

import android.os.Environment
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.Thread.UncaughtExceptionHandler

class CustomExceptionHandler : Thread.UncaughtExceptionHandler {
    private val eH: UncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()

    override fun uncaughtException(t: Thread, e: Throwable) {
        val log = File("${Environment.getExternalStorageDirectory().absolutePath}/warsmash_crash.log");
        if (log.exists()) {
            log.delete()
        }
        val writer = StringWriter()
        val pw = PrintWriter(writer)
        e.printStackTrace(pw)
        log.writeText(writer.toString())
        eH.uncaughtException(t, e)
    }
}