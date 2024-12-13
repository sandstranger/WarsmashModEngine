package net.warsmash.phone.android.engine.activity

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.etheller.warsmash.WarsmashGdxMenuScreen
import com.etheller.warsmash.WarsmashGdxMultiScreenGame
import net.warsmash.phone.android.engine.loadExtensions
import java.util.function.Consumer

/** Launches the Android application.  */
class EngineActivity : AndroidApplication() {

//    val audio: AndroidOpenALAudio = AndroidOpenALAudio()

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