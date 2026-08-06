package de.fabmax.kool.modules.compose.modifiers

import de.fabmax.kool.input.KeyEvent
import me.dvyy.compose.mini.modifier.DelegatableNode
import me.dvyy.compose.mini.modifier.Modifier
import me.dvyy.compose.mini.modifier.ModifierNodeElement

/**
 * Adding this [modifier][Modifier] to the [modifier][Modifier] parameter of a component will allow
 * it to intercept hardware key events when it (or one of its children) is focused.
 *
 * @param onKeyEvent This callback is invoked when the user interacts with the hardware keyboard.
 *   While implementing this callback, return true to stop propagation of this event. If you return
 *   false, the key event will be sent to this [onKeyEvent]'s parent.
 * @sample androidx.compose.ui.samples.KeyEventSample
 */
fun Modifier.onKeyEvent(onKeyEvent: (KeyEvent) -> Boolean): Modifier =
    this then KeyInputElement(onKeyEvent = onKeyEvent, onPreKeyEvent = null)

/**
 * Adding this [modifier][Modifier] to the [modifier][Modifier] parameter of a component will allow
 * it to intercept hardware key events when it (or one of its children) is focused.
 *
 * @param onPreviewKeyEvent This callback is invoked when the user interacts with the hardware
 *   keyboard. It gives ancestors of a focused component the chance to intercept a [KeyEvent].
 *   Return true to stop propagation of this event. If you return false, the key event will be sent
 *   to this [onPreviewKeyEvent]'s child. If none of the children consume the event, it will be sent
 *   back up to the root [KeyInputModifierNode] using the onKeyEvent callback.
 * @sample androidx.compose.ui.samples.KeyEventSample
 */
fun Modifier.onPreviewKeyEvent(onPreviewKeyEvent: (KeyEvent) -> Boolean): Modifier =
    this then KeyInputElement(onKeyEvent = null, onPreKeyEvent = onPreviewKeyEvent)

/**
 * Implement this interface to create a [Modifier.Node] that can intercept hardware Key events.
 *
 * The event is routed to the focused item. Before reaching the focused item, [onPreKeyEvent]() is
 * called for parents of the focused item. If the parents don't consume the event, [onPreKeyEvent]()
 * is called for the focused item. If the event is still not consumed, [onKeyEvent]() is called on
 * the focused item's parents.
 */
interface KeyInputModifierNode : DelegatableNode {

    /**
     * This function is called when a [KeyEvent] is received by this node during the upward pass.
     * While implementing this callback, return true to stop propagation of this event. If you
     * return false, the key event will be sent to this [KeyInputModifierNode]'s parent.
     */
    fun onKeyEvent(event: KeyEvent): Boolean

    /**
     * This function is called when a [KeyEvent] is received by this node during the downward pass.
     * It gives ancestors of a focused component the chance to intercept an event. Return true to
     * stop propagation of this event. If you return false, the event will be sent to this
     * [KeyInputModifierNode]'s child. If none of the children consume the event, it will be sent
     * back up to the root using the [onKeyEvent] function.
     */
    fun onPreKeyEvent(event: KeyEvent): Boolean
}


private data class KeyInputElement(
    val onKeyEvent: ((KeyEvent) -> Boolean)?,
    val onPreKeyEvent: ((KeyEvent) -> Boolean)?,
) : ModifierNodeElement<KeyInputNode>() {
    override fun create() = KeyInputNode(onKeyEvent, onPreKeyEvent)

    override fun update(node: KeyInputNode) {
        node.onEvent = onKeyEvent
        node.onPreEvent = onPreKeyEvent
    }
}

private class KeyInputNode(
    var onEvent: ((KeyEvent) -> Boolean)?,
    var onPreEvent: ((KeyEvent) -> Boolean)?,
) : KeyInputModifierNode, Modifier.Node() {
    override fun onKeyEvent(event: KeyEvent): Boolean = this.onEvent?.invoke(event) ?: false

    override fun onPreKeyEvent(event: KeyEvent): Boolean = this.onPreEvent?.invoke(event) ?: false
}
