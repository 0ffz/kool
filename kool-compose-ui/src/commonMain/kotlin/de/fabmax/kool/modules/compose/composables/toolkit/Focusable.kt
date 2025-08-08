package de.fabmax.kool.modules.compose.composables.toolkit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import de.fabmax.kool.input.KeyEvent
import de.fabmax.kool.modules.compose.Layout
import de.fabmax.kool.modules.compose.LocalUiSurface
import de.fabmax.kool.modules.compose.modifiers.Modifier
import de.fabmax.kool.modules.ui2.*

@Composable
fun Focusable(
    focused: Boolean,
    onFocusChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val surface = LocalUiSurface.current
    val focused by rememberUpdatedState(focused)
    val onFocusChanged by rememberUpdatedState(onFocusChanged)
    val focusable = remember(surface) {
        FocusRequester(null, surface).apply {
            this.onFocusChanged = onFocusChanged
        }
    }
    if (focused) surface.requestFocus(focusable)
    else surface.unfocus(focusable)
    Layout({ _, _ -> focusable }, modifier) {
        content()
    }
}

class FocusRequester(
    parent: UiNode?,
    surface: UiSurface,
) : UiNode(parent, surface), Focusable {
    var onFocusChanged: ((Boolean) -> Unit)? = null
    override val modifier: UiModifier = UiModifier(surface)
    override val isFocused: MutableStateValue<Boolean> = MutableStateValue(false)

    override fun onFocusGain() {
        super.onFocusGain()
        onFocusChanged?.invoke(true)
    }

    override fun onFocusLost() {
        super.onFocusLost()
        onFocusChanged?.invoke(false)
    }

    override fun onKeyEvent(keyEvent: KeyEvent) {
    }
}
