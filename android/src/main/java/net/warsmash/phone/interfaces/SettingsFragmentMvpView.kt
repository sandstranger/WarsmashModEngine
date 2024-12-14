package net.warsmash.phone.interfaces

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface SettingsFragmentMvpView : MvpView {
    @AddToEndSingle
    fun updatePreference (prefsKey : String)
}