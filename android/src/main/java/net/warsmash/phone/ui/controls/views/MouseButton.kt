package net.warsmash.phone.ui.controls.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class MouseButton(context: Context, attrs: AttributeSet) : TouchScreenImageButton(context, attrs) {

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (!interactable) return false
        when (event.actionMasked){
            MotionEvent.ACTION_DOWN -> {
                keysEmulator?.onTouchDown?.invoke(keyCode)
            }
        }
        return true
    }
}