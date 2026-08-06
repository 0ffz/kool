package de.fabmax.kool.modules.compose.modifiers

import de.fabmax.kool.modules.ui2.PointerEvent
import me.dvyy.compose.mini.modifier.DelegatableNode
import me.dvyy.compose.mini.modifier.Modifier
import me.dvyy.compose.mini.modifier.ModifierNodeElement

interface ClickModifierNode : DelegatableNode {
    fun onEvent(event: PointerEvent)
}

data class PointerEventElement(
    val onEvent: (PointerEvent) -> Unit,
) : ModifierNodeElement<PointerEventNode>() {
    override fun create(): PointerEventNode = PointerEventNode(onEvent)
    override fun update(node: PointerEventNode) {
        node.onPointerEvent = onEvent
    }
}

class PointerEventNode(
    var onPointerEvent: (PointerEvent) -> Unit,
) : ClickModifierNode, Modifier.Node() {
    override fun onEvent(event: PointerEvent) {
        return onPointerEvent(event)
    }
}

fun Modifier.onClick(onClick: () -> Unit): Modifier {
    return then(PointerEventElement {
        if (it.pointer.isAnyButtonClicked) {
            onClick()
            it.isConsumed = true
        }
    })
}

fun Modifier.onPointerEvent(onEvent: (PointerEvent) -> Unit): Modifier {
    return then(PointerEventElement(onEvent))
}