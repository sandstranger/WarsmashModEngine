package net.warsmash.phone.android.engine.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Environment
import android.system.Os
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import barsoosayque.libgdxoboe.OboeAudio
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.badlogic.gdx.backends.android.AndroidAudio
import com.etheller.warsmash.KeysEmulator
import com.etheller.warsmash.WarsmashGdxMenuScreen
import com.etheller.warsmash.WarsmashGdxMultiScreenGame
import kotlinx.coroutines.launch
import net.warsmash.phone.android.engine.loadExtensions
import net.warsmash.phone.android.engine.setFullscreen
import net.warsmash.phone.databinding.ScreenControlsBinding
import net.warsmash.phone.ui.controls.ScreenControlsManager
import net.warsmash.phone.utils.GAME_FILES_SHARED_PREFS_KEY
import net.warsmash.phone.utils.HIDE_SCREEN_CONTROLS_KEY
import net.warsmash.phone.utils.extensions.displayInCutoutArea
import java.io.File
import java.util.function.Consumer


/** Launches the Android application.  */
class EngineActivity : AndroidApplication() {
    private lateinit var loggerProcess : Process
    private lateinit var prefsManager : SharedPreferences
    private lateinit var screenControlsManager : ScreenControlsManager
    private val keysEmulator : KeysEmulator = KeysEmulator()

    override fun createAudio(context: Context, config: AndroidApplicationConfiguration): AndroidAudio =
        OboeAudio(context.assets)

    override fun onCreate(savedInstanceState: Bundle?) {
        setFullscreen(window.decorView)
        super.onCreate(savedInstanceState)

        loggerProcess = createLoggerProcess(Environment.getExternalStorageDirectory().absolutePath
                + "/" + "warsmash.log")
        prefsManager = PreferenceManager.getDefaultSharedPreferences(this)

        displayInCutoutArea(prefsManager)
        launchGame()
        initScreenControls()
    }

    public override fun onDestroy() {
        super.onDestroy()
        loggerProcess.destroy()
    }

    private fun launchGame (){
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

    private fun createLoggerProcess(pathToLog: String): Process {
        val file = File(pathToLog)
        if (file.exists()){
            file.delete()
        }
        val processBuilder = ProcessBuilder()
        processBuilder.command("/system/bin/sh", "-c", "logcat *:W -d -f $pathToLog")
        processBuilder.redirectErrorStream(true)
        return processBuilder.start()
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