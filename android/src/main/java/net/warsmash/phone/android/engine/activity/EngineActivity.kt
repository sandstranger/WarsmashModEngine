package net.warsmash.phone.android.engine.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.system.Os
import android.util.Log
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import barsoosayque.libgdxoboe.OboeAudio
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.badlogic.gdx.backends.android.AndroidAudio
import com.etheller.warsmash.KeysEmulator
import com.etheller.warsmash.WarsmashGdxMenuScreen
import com.etheller.warsmash.WarsmashGdxMultiScreenGame
import net.warsmash.phone.android.engine.loadExtensions
import net.warsmash.phone.android.engine.setFullscreen
import net.warsmash.phone.databinding.ScreenControlsBinding
import net.warsmash.phone.ui.controls.ScreenControlsManager
import net.warsmash.phone.utils.CUSTOM_RESOLUTION_PREFS_KEY
import net.warsmash.phone.utils.GAME_FILES_SHARED_PREFS_KEY
import net.warsmash.phone.utils.HIDE_SCREEN_CONTROLS_KEY
import net.warsmash.phone.utils.extensions.displayInCutoutArea
import java.util.function.Consumer


/** Launches the Android application.  */
class EngineActivity : AndroidApplication() {
    private val RESOLUTION_DELIMITER = "x"

    private lateinit var prefsManager : SharedPreferences
    private lateinit var screenControlsManager : ScreenControlsManager
    private val keysEmulator : KeysEmulator = KeysEmulator()

    override fun createAudio(context: Context, config: AndroidApplicationConfiguration): AndroidAudio =
        OboeAudio(context.assets)

    override fun onCreate(savedInstanceState: Bundle?) {
        setFullscreen(window.decorView)
        super.onCreate(savedInstanceState)

        prefsManager = PreferenceManager.getDefaultSharedPreferences(this)

        displayInCutoutArea(prefsManager)
        launchGame()
        initScreenControls()
    }

    private fun setCustomResolution (){
        val customResolution = prefsManager.getString(CUSTOM_RESOLUTION_PREFS_KEY, "")
        if (!customResolution.isNullOrEmpty() && customResolution.contains(RESOLUTION_DELIMITER)) {
            try {
                val resolutionsArray = customResolution.split(RESOLUTION_DELIMITER)
                Os.setenv("SCREEN_WIDTH", resolutionsArray[0],true)
                Os.setenv("SCREEN_HEIGHT",resolutionsArray[1],true)
            } catch (e: Exception) {
            }
        }
        else{
            Os.setenv("SCREEN_WIDTH", "",true)
            Os.setenv("SCREEN_HEIGHT","",true)
        }
    }

    private fun launchGame (){
        setCustomResolution()
        Os.setenv(GAME_FILES_SHARED_PREFS_KEY,
            prefsManager.getString(GAME_FILES_SHARED_PREFS_KEY,""), true)
        Os.setenv("GAME_VERSION", getGameVersion().toString(), true)
        Os.setenv("PLAYERS_COUNT", prefsManager.getString("players_count","28"), true)
        Os.setenv("SERVER",prefsManager.getString("server","warsmash.net"), true)

        val configuration = AndroidApplicationConfiguration()
        configuration.useGL30 = true
        loadExtensions()
        val warsmashGdxMultiScreenGame = WarsmashGdxMultiScreenGame(Consumer { game: WarsmashGdxMultiScreenGame ->
            game.screen = WarsmashGdxMenuScreen(null, game, keysEmulator)
        })
        initialize(warsmashGdxMultiScreenGame, configuration)
    }

    private fun getGameVersion () : Int {
        val gameVersion = prefsManager.getString("Game version", "");
        return if (gameVersion == "TFT") 1 else 0
    }

    private fun initScreenControls (){
        val hideScreenControls = prefsManager.getBoolean(HIDE_SCREEN_CONTROLS_KEY,false)

        if (!hideScreenControls) {
            val binding = ScreenControlsBinding.inflate(layoutInflater)

            window.addContentView(
                binding.root,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )

            binding.screenControlsRoot.post {
                screenControlsManager = ScreenControlsManager(binding, keysEmulator)
                screenControlsManager.enableScreenControls()
            }
        }
    }
}