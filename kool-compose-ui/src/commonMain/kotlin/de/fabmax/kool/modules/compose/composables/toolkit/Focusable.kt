package de.fabmax.kool.modules.compose.composables.toolkit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import me.dvyy.compose.mini.modifier.Modifier
import me.dvyy.compose.mini.modifier.ModifierNodeElement

@Composable
fun rememberFocusRequester(): FocusRequester {
    return remember { FocusRequester() }
}

interface Focusable {
    fun onFocusChange(isFocused: Boolean)
}

@Stable
fun Modifier.focusable(onFocusChange: (Boolean) -> Unit) = then(FocusableElement(onFocusChange))

data class FocusableElement(
    val onFocusChange: ((Boolean) -> Unit)? = null,
) : ModifierNodeElement<FocusableNode>() {
    override fun create(): FocusableNode = FocusableNode(onFocusChange)

    override fun update(node: FocusableNode) {
        node.onFocusChange = onFocusChange
    }
}

class FocusableNode(
    var onFocusChange: ((Boolean) -> Unit)? = null,
) : Focusable, Modifier.Node() {

    override fun onFocusChange(isFocused: Boolean) {
        onFocusChange?.invoke(isFocused)
    }

}

class FocusRequester {
    val request = MutableSharedFlow<Unit>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    fun requestFocus() {
        request.tryEmit(Unit)
    }
}
