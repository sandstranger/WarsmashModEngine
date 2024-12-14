package net.warsmash.phone.android.engine

import android.content.Context
import android.os.Process
import com.badlogic.gdx.audio.Sound
import com.etheller.warsmash.viewer5.AudioContext
import com.etheller.warsmash.viewer5.AudioDestination
import com.etheller.warsmash.viewer5.gl.AudioExtension
import com.etheller.warsmash.viewer5.gl.Extensions
import com.etheller.warsmash.viewer5.gl.WireframeExtension
import net.warsmash.phone.android.engine.activity.EngineActivity
import net.warsmash.phone.utils.extensions.startActivity
import org.lwjgl.opengl.GL11

fun killEngine() = Process.killProcess(Process.myPid())


fun startEngine(context: Context) {
    context.startActivity<EngineActivity>()
}

fun loadExtensions() {

    Extensions.audio = object : AudioExtension {
        override fun getDuration(sound: Sound?): Float {
            if (sound == null) {
                return 1.0f
            }
            return 2.0f
        }

        override fun play(
            buffer: Sound, volume: Float, pitch: Float, x: Float, y: Float,
            z: Float, is3dSound: Boolean, maxDistance: Float, refDistance: Float,
            looping: Boolean
        ): Long {
            return if (looping) {
                buffer.loop(volume, pitch, 0.0f)
            } else {
                buffer.play(volume, pitch, 0.0f)
            }
        }

        override fun createContext(world: Boolean): AudioContext {
            val listener = AudioContext.Listener.DO_NOTHING

            return AudioContext(listener, object : AudioDestination {
            })
        }
    }

    Extensions.angleInstancedArrays = ANGLEInstancedArraysGLES30()

    Extensions.wireframeExtension =
        WireframeExtension { face, mode -> GL11.glPolygonMode(face, mode) }

    Extensions.dynamicShadowExtension = DynamicShadowExtensionGLES30()

    Extensions.GL_LINE = GL11.GL_LINE
    Extensions.GL_FILL = GL11.GL_FILL

//    Extensions.GL_LINE = 0
  //  Extensions.GL_FILL = 0
}
