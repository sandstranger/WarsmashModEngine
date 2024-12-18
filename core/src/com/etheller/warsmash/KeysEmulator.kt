package com.etheller.warsmash

class KeysEmulator {
    var onKeyDownListener : ((keyCode : Int) -> Unit)? = null
    var onKeyUpListener : ((keyCode : Int) -> Unit)? = null
    var onTouchDown : ((keyCode : Int) -> Unit)? = null
    var onTouchUp : ((keyCode : Int) -> Unit)? = null
    var onScrolledListener: ((scrollAmount : Float) -> Unit) ? = null
}