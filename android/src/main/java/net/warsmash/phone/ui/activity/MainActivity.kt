package net.warsmash.phone.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import net.warsmash.phone.R
import net.warsmash.phone.android.engine.startEngine
import net.warsmash.phone.databinding.MainActivityBinding
import net.warsmash.phone.utils.extensions.requestExternalStoragePermission

internal class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startGameButton.setOnClickListener {
            startEngine(this)
        }

        this.requestExternalStoragePermission()
        changeFragment()
    }

    private fun changeFragment() {
        val fragmentContainterResId = R.id.fragments_container
        var fragment = supportFragmentManager.findFragmentById(fragmentContainterResId)

        if (fragment == null) {
          /*  fragment = SettingsFragment()
            supportFragmentManager.beginTransaction().apply {
                replace(fragmentContainterResId, fragment)
                commit()
            }*/
        }
    }
}
