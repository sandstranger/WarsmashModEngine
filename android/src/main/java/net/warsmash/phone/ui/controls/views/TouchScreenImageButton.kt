package net.warsmash.phone.ui.controls.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatImageButton
import com.etheller.warsmash.KeysEmulator

open class TouchScreenImageButton (context: Context, attrs : AttributeSet) : AppCompatImageButton(context, attrs), View.OnTouchListener {
    var interactable : Boolean = true
    protected var keyCode : Int = 0
    protected var keysEmulator : KeysEmulator? = null

    init {
        setOnTouchListener(this)
    }

    open override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (!interactable) return false

        when (event.actionMasked){
            MotionEvent.ACTION_BUTTON_PRESS,
            MotionEvent.ACTION_DOWN -> {
                keysEmulator?.onKeyDownListener?.invoke(keyCode)
            }
            MotionEvent.ACTION_UP -> {
                keysEmulator?.onKeyUpListener?.invoke(keyCode)
            }
        }
        return true
    }

    fun setKeycode (keyCode: Int) : TouchScreenImageButton {
        this.keyCode = keyCode
        return this
    }

    fun setKeysEmulator (keysEmulator: KeysEmulator?) : TouchScreenImageButton {
        this.keysEmulator = keysEmulator
        return this
    }
}