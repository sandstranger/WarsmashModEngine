package net.warsmash.phone.ui.fragment

import android.app.Activity
import android.content.Intent
import android.content.Intent.createChooser
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import net.warsmash.phone.interfaces.SettingsFragmentMvpView
import net.warsmash.phone.presenter.SettingsFragmentPresenter
import net.warsmash.phone.utils.GAME_FILES_SHARED_PREFS_KEY
import com.obsez.android.lib.filechooser.ChooserDialog
import moxy.presenter.InjectPresenter
import net.warsmash.phone.R
import net.warsmash.phone.utils.extensions.changeInputTypeToDecimal


class SettingsFragment : MvpAppCompatFragment(), SettingsFragmentMvpView,
    SharedPreferences.OnSharedPreferenceChangeListener {

    private val CHOOSE_DIRECTORY_REQUEST_CODE = 4321
    private val CHOOSE_DIRECTORY_TEXT = "Choose directory"
    @InjectPresenter
    lateinit var presenter: SettingsFragmentPresenter

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)
        addPreferencesFromResource(R.xml.settings)

        val gameFilesPreference = findPreference<Preference>(GAME_FILES_SHARED_PREFS_KEY)
        gameFilesPreference?.setOnPreferenceClickListener {
            val isTelevision =
                requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_LEANBACK)
            if (isTelevision) {
                ChooserDialog(this.requireContext())
                    .withFilter(true, false)
                    .withStartFile(Environment.getExternalStorageDirectory().absolutePath) // to handle the result(s)
                    .withChosenListener(ChooserDialog.Result { path, _ ->
                        presenter.saveGamePath(path,requireContext(),preferenceScreen.sharedPreferences!!)
                    })
                    .build()
                    .show()
            } else {
                with(Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)) {
                    addCategory(Intent.CATEGORY_DEFAULT)
                    startActivityForResult(
                        createChooser(this, CHOOSE_DIRECTORY_TEXT),
                        CHOOSE_DIRECTORY_REQUEST_CODE
                    )
                }
            }

            true
        }
        updatePreference(gameFilesPreference!!, GAME_FILES_SHARED_PREFS_KEY)

        val playersCount = findPreference<EditTextPreference>("players_count")
        playersCount?.changeInputTypeToDecimal()

        findPreference<Preference>("screen_controls_settings")?.setOnPreferenceClickListener {
            presenter.onConfigureScreenControlsClicked(requireContext())
            true
        }

        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.copy_game_assets -> {
                presenter.copyGameAssets(requireContext(), preferenceScreen.sharedPreferences!!)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when{
            resultCode != Activity.RESULT_OK -> return
            requestCode == CHOOSE_DIRECTORY_REQUEST_CODE ->
            {
                presenter.saveGamePath(data!!,requireContext(),this.preferenceScreen.sharedPreferences!!)
            }
        }
    }

    override fun updatePreference (prefsKey : String) =
        updatePreference(findPreference(prefsKey)!!,prefsKey)

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        updatePreference(key!!)
    }

    private fun updatePreference (preference: Preference, prefsKey: String){
        try {
            preference.summary = preferenceScreen.sharedPreferences?.getString(prefsKey, "") ?: ""
        }
        catch (e: Exception){

        }
    }
}