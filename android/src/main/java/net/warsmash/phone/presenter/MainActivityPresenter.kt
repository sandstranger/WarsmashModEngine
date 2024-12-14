package net.warsmash.phone.presenter

import android.app.Activity
import android.content.Context
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.MvpView
import net.warsmash.phone.android.engine.startEngine
import net.warsmash.phone.utils.extensions.requestExternalStoragePermission

@InjectViewState
class MainActivityPresenter : MvpPresenter<MvpView>() {
    internal fun onStartGameBtnClicked(context: Context) = startEngine(context)

    internal fun requestExternalStorage (activity : Activity) = activity.requestExternalStoragePermission()
}