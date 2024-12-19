package net.warsmash.phone.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import net.warsmash.phone.R
import net.warsmash.phone.android.engine.startEngine
import net.warsmash.phone.databinding.TvActivityBinding
import net.warsmash.phone.ui.fragment.SettingsFragment
import net.warsmash.phone.utils.extensions.requestExternalStoragePermission

class TVActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = TvActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startGameButton.setOnClickListener {
            startEngine(this)
        }

        requestExternalStoragePermission()
        changeFragment()
    }

    private fun changeFragment() {
        val fragmentContainterResId = R.id.fragments_container
        var fragment = supportFragmentManager.findFragmentById(fragmentContainterResId)

        if (fragment == null) {
            fragment = SettingsFragment()
            supportFragmentManager.beginTransaction().apply {
                replace(fragmentContainterResId, fragment)
                commit()
            }
        }
    }
}