package net.warsmash.phone.ui.controls

import android.graphics.Color
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.preference.PreferenceManager
import com.badlogic.gdx.Input
import com.etheller.warsmash.KeysEmulator
import net.warsmash.phone.databinding.ScreenControlsBinding
import net.warsmash.phone.ui.controls.views.ScrollableImageButton
import net.warsmash.phone.ui.controls.views.TouchScreenImageButton

const val VIRTUAL_SCREEN_WIDTH = 1024
const val VIRTUAL_SCREEN_HEIGHT = 768
const val CONTROL_DEFAULT_SIZE = 70

class ScreenControlsManager(
    private val screenControlsBinding: ScreenControlsBinding,
    private val keysEmulator: KeysEmulator?
) {

    private var callback: ConfigureCallback? = null
    private val controlsItems = arrayListOf<ControlsItem>()

    init {
       controlsItems += ControlsItem(
            "mouse_button", screenControlsBinding.rightMouseButton.setKeycode(
               Input.Buttons.RIGHT).setKeysEmulator(keysEmulator),
            800, 350, 80
        )

       controlsItems += ControlsItem(
            "camera_left_button", screenControlsBinding.cameraLeftButton.setKeycode(
               Input.Keys.LEFT).setKeysEmulator(keysEmulator),
            30, 550, 70
        )
       controlsItems += ControlsItem(
            "camera_right_button", screenControlsBinding.cameraRightButton.setKeycode(
               Input.Keys.RIGHT).setKeysEmulator(keysEmulator),
            150, 550, 70
        )

       controlsItems += ControlsItem(
            "camera_down_button", screenControlsBinding.cameraDownButton.setKeycode(
               Input.Keys.DOWN).setKeysEmulator(keysEmulator),
            90, 640, 70
        )

       controlsItems += ControlsItem(
            "camera_up_button", screenControlsBinding.cameraUpButton.setKeycode(
               Input.Keys.UP).setKeysEmulator(keysEmulator),
            90, 455, 70
        )

       controlsItems += ControlsItem(
            "scroll_up_button", screenControlsBinding.scrollUpButton.setScrollAmount(
               -1.5f).setKeysEmulator(keysEmulator),
            900, 200, 70
        )

       controlsItems += ControlsItem(
            "scroll_down_button", screenControlsBinding.scrollDownButton.setScrollAmount(
               1.5f).setKeysEmulator(keysEmulator),
            900, 340, 70
        )

        controlsItems += ControlsItem(
            "hide_all_btns_button", screenControlsBinding.hideAllBtnsButton,
            450, 10, 70
        )

        controlsItems.forEach {
            it.loadPrefs()
        }
    }

    fun editScreenControls() {
        callback = ConfigureCallback(screenControlsBinding.screenControlsRoot)
        controlsItems.forEach {
            it.view.setOnTouchListener(callback)

            if (it.view is TouchScreenImageButton) {
                it.view.interactable = false
            }

            if (it.view is ScrollableImageButton) {
                it.view.interactable = false
            }
        }
        screenControlsBinding.buttonsHolder.visibility = View.VISIBLE
        screenControlsBinding.screenControlsRoot.setBackgroundColor(Color.GRAY)
    }

    fun enableScreenControls() {
        screenControlsBinding.buttonsHolder.visibility = View.GONE

        screenControlsBinding.hideAllBtnsButton.setOnClickListener {
            for (button in controlsItems) {
                if (button.view === screenControlsBinding.hideAllBtnsButton) {
                    continue
                }

                button.view.visibility =
                    if (button.view.visibility == View.GONE) View.VISIBLE else View.GONE
            }
        }
    }

    fun changeOpacity(delta: Float) {
        val view = callback?.currentView ?: return
        val el = view.tag as ControlsItem
        el.changeOpacity(delta)
        el.updateView()
    }

    fun changeSize(delta: Int) {
        val view = callback?.currentView ?: return
        val el = view.tag as ControlsItem
        el.changeSize(delta)
        el.updateView()
    }

    fun resetItems() {
        controlsItems.forEach {
            it.resetPrefs()
        }
    }

    private inner class ControlsItem(
        val uniqueId: String,
        val view: View,
        val defaultX: Int,
        val defaultY: Int,
        val defaultSize: Int = CONTROL_DEFAULT_SIZE,
        val defaultOpacity: Float = 0.5f,
        val visible: Boolean = true
    ) {

        private var opacity = defaultOpacity
        var size = defaultSize
        var x = defaultX
        var y = defaultY

        init {
            view.tag = this
        }

        fun changeOpacity(delta: Float) {
            opacity = Math.max(0f, Math.min(opacity + delta, 1.0f))
            savePrefs()
        }

        fun changeSize(delta: Int) {
            size = Math.max(0, size + delta)
            savePrefs()
        }

        fun changePosition(virtualX: Int, virtualY: Int) {
            x = virtualX
            y = virtualY
            savePrefs()
        }

        fun updateView() {
            val v = view
            val realScreenWidth = (v.parent as View).width
            val realScreenHeight = (v.parent as View).height
            val realX = x * realScreenWidth / VIRTUAL_SCREEN_WIDTH
            val realY = y * realScreenHeight / VIRTUAL_SCREEN_HEIGHT

            val screenSize = (1.0 * size * realScreenWidth / VIRTUAL_SCREEN_WIDTH).toInt()
            val params = FrameLayout.LayoutParams(screenSize, screenSize)

            params.leftMargin = realX
            params.topMargin = realY

            v.layoutParams = params

            v.alpha = opacity
        }

        private fun savePrefs() {
            val v = view
            val prefs = PreferenceManager.getDefaultSharedPreferences(v.context)
            with(prefs.edit()) {
                putFloat("osc:$uniqueId:opacity", opacity)
                putInt("osc:$uniqueId:size", size)
                putInt("osc:$uniqueId:x", x)
                putInt("osc:$uniqueId:y", y)

                commit()
            }
        }

        fun loadPrefs() {
            val prefs = PreferenceManager.getDefaultSharedPreferences(view.context)

            opacity = prefs.getFloat("osc:$uniqueId:opacity", defaultOpacity)
            size = prefs.getInt("osc:$uniqueId:size", defaultSize)
            x = prefs.getInt("osc:$uniqueId:x", defaultX)
            y = prefs.getInt("osc:$uniqueId:y", defaultY)

            updateView()
        }

        fun resetPrefs() {
            val prefs = PreferenceManager.getDefaultSharedPreferences(view.context)

            with(prefs.edit()) {
                remove("osc:$uniqueId:opacity")
                remove("osc:$uniqueId:size")
                remove("osc:$uniqueId:x")
                remove("osc:$uniqueId:y")

                commit()
            }

            loadPrefs()
        }
    }

    private data class ScreenSize(val width: Int, val height: Int)

    private class ConfigureCallback(private val rootView : View) : View.OnTouchListener {

        var currentView: View? = null
        private var origX: Float = 0.0f
        private var origY: Float = 0.0f
        private var startX: Float = 0.0f
        private var startY: Float = 0.0f

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    currentView?.setBackgroundColor(Color.TRANSPARENT)
                    currentView = v
                    v.setBackgroundColor(Color.RED)
                    origX = v.x
                    origY = v.y
                    startX = event.rawX
                    startY = event.rawY
                }

                MotionEvent.ACTION_MOVE -> if (currentView != null) {
                    val view = currentView!!
                    val x = ((event.rawX - startX) + origX).toInt()
                    val y = ((event.rawY - startY) + origY).toInt()

                    val el = view.tag as ControlsItem
                    el.changePosition(
                        x * VIRTUAL_SCREEN_WIDTH / rootView.width,
                        y * VIRTUAL_SCREEN_HEIGHT /rootView.height
                    )
                    el.updateView()
                }
            }

            return true
        }
    }

}