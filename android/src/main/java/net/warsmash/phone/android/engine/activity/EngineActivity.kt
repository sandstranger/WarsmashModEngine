package net.warsmash.phone.android.engine.activity

import android.os.Bundle
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.etheller.warsmash.WarsmashGdxMenuScreen
import com.etheller.warsmash.WarsmashGdxMultiScreenGame
import com.etheller.warsmash.viewer5.AudioContext
import com.etheller.warsmash.viewer5.AudioDestination
import com.etheller.warsmash.viewer5.gl.ANGLEInstancedArrays
import com.etheller.warsmash.viewer5.gl.AudioExtension
import com.etheller.warsmash.viewer5.gl.Extensions
import com.etheller.warsmash.viewer5.gl.WireframeExtension
import net.warsmash.phone.android.loadExtensions
import java.util.function.Consumer

/** Launches the Android application.  */
class EngineActivity : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val configuration = AndroidApplicationConfiguration()
        configuration.useGL30 = true
        loadExtensions()
        val warsmashGdxMultiScreenGame = WarsmashGdxMultiScreenGame(Consumer { game: WarsmashGdxMultiScreenGame ->
                val menuScreen = WarsmashGdxMenuScreen(null, game)
                game.screen = menuScreen
            })
        initialize(warsmashGdxMultiScreenGame, configuration)
    }
}