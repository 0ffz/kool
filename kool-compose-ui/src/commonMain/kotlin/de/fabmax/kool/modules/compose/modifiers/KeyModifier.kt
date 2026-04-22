package de.fabmax.kool.modules.compose.modifiers

import de.fabmax.kool.modules.ui2.PointerEvent
import me.dvyy.compose.mini.modifier.Modifier
import me.dvyy.compose.mini.modifier.ModifierNodeElement

interface ClickModifierNode {
    fun onClick(event: PointerEvent): Boolean
}

data class ClickElement(val clickAction: () -> Boolean) : ModifierNodeElement<ClickNode>() {
    override fun create(): ClickNode = ClickNode(clickAction)
    override fun update(node: ClickNode) {
        node.clickAction = clickAction
    }
}

class ClickNode(
    var clickAction: () -> Boolean,
) : ClickModifierNode, Modifier.Node() {
    override fun onClick(event: PointerEvent): Boolean {
        return clickAction()
    }
}

//inline fun Modifier.onClick(crossinline onClick: () -> Unit): Modifier {
//    return then(ClickElement({ onClick(); true }))
//}

fun Modifier.onClick(onClick: () -> Boolean): Modifier {
    return then(ClickElement(onClick))
}