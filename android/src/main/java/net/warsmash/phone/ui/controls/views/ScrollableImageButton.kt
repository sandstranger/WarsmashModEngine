package net.warsmash.phone.ui.controls.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatImageButton
import com.etheller.warsmash.KeysEmulator

class ScrollableImageButton (context: Context, attrs : AttributeSet) : AppCompatImageButton(context, attrs), View.OnTouchListener {
    var interactable : Boolean = true
    private var scrollAmount : Float = 0.0F
    private var keysEmulator : KeysEmulator? = null

    init {
        setOnTouchListener(this)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (!interactable) return false

        when (event.actionMasked){
            MotionEvent.ACTION_DOWN -> {
                keysEmulator?.onScrolledListener?.invoke(scrollAmount)
            }
        }
        return true
    }

    fun setScrollAmount (scrollAmount: Float) : ScrollableImageButton {
        this.scrollAmount = scrollAmount
        return this
    }

    fun setKeysEmulator (keysEmulator: KeysEmulator?) : ScrollableImageButton {
        this.keysEmulator = keysEmulator
        return this
    }
}