package net.warsmash.phone.ui.controls.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MouseButton(context: Context, attrs: AttributeSet) : TouchScreenImageButton(context, attrs) {

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (!interactable) return false
        when (event.actionMasked){
            MotionEvent.ACTION_DOWN -> {
                CoroutineScope(Dispatchers.Default).launch {
                    keysEmulator?.onTouchDown?.invoke(keyCode)
                }
            }

            MotionEvent.ACTION_UP -> {
                CoroutineScope(Dispatchers.Default).launch {
                    keysEmulator?.onTouchUp?.invoke(keyCode)
                }
            }
        }
        return true
    }
}