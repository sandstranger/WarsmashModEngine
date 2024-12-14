package net.warsmash.phone.android.engine.activity

import android.content.Context
import android.os.Bundle
import barsoosayque.libgdxoboe.OboeAudio
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.badlogic.gdx.backends.android.AndroidAudio
import com.etheller.warsmash.WarsmashGdxMenuScreen
import com.etheller.warsmash.WarsmashGdxMultiScreenGame
import net.warsmash.phone.android.engine.loadExtensions
import java.util.function.Consumer

/** Launches the Android application.  */
class EngineActivity : AndroidApplication() {

    override fun createAudio(context: Context, config: AndroidApplicationConfiguration): AndroidAudio =
        OboeAudio(context.assets)

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