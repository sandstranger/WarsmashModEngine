package net.warsmash.phone.android

import android.content.Context
import android.os.Process
import net.warsmash.phone.android.engine.activity.EngineActivity
import net.warsmash.phone.utils.extensions.startActivity

fun killEngine() = Process.killProcess(Process.myPid())

fun startEngine(context: Context) {
    context.startActivity<EngineActivity>()
}