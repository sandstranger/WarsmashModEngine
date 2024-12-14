package net.warsmash.phone.presenter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Environment
import net.warsmash.phone.interfaces.SettingsFragmentMvpView
import net.warsmash.phone.utils.GAME_FILES_SHARED_PREFS_KEY
import net.warsmash.phone.utils.copyGameAssets
import moxy.InjectViewState
import moxy.MvpPresenter
import net.warsmash.phone.utils.GAME_FILES_FOLDER_NAME

@InjectViewState
class SettingsFragmentPresenter : MvpPresenter<SettingsFragmentMvpView>() {

    fun onConfigureScreenControlsClicked(context: Context) {
        //context.startActivity<ConfigureControlsActivity>(finishParentActivity = false)
    }

    fun saveGamePath(data: Intent, context: Context, preferences: SharedPreferences) {
        data.data?.also { uri ->
            val pattern = Regex("[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}")
            val storageDir = Environment.getExternalStorageDirectory()
            val storagePath = storageDir.absolutePath
            val modifiedStoragePath = "/storage"
            val pathSegment = uri.lastPathSegment
            val currentGamePath = if (pattern.containsMatchIn(pathSegment ?: "")) {
                modifiedStoragePath + "/" + pathSegment?.replace(":", "/")
            } else {
                storagePath + "/" + pathSegment?.replace("primary:", "")
            }

            saveGamePath(currentGamePath, context, preferences)
        }
    }

    fun saveGamePath(currentGamePath: String, context: Context, preferences: SharedPreferences) {
        with(preferences.edit()) {
            putString(GAME_FILES_SHARED_PREFS_KEY, currentGamePath)
            apply()
            copyGameAssets(context, GAME_FILES_FOLDER_NAME, currentGamePath)
            viewState?.updatePreference(GAME_FILES_SHARED_PREFS_KEY)
        }
    }

    fun copyGameAssets(context: Context, preferences: SharedPreferences) {
        val currentGamePath = preferences.getString(GAME_FILES_SHARED_PREFS_KEY, "")

        if (currentGamePath.isNullOrEmpty()) {
            return
        }

        copyGameAssets(context, GAME_FILES_FOLDER_NAME, currentGamePath)
    }
}