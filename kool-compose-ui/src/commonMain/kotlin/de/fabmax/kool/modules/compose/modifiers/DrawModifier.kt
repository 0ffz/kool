package de.fabmax.kool.modules.compose.modifiers

import de.fabmax.kool.modules.compose.composables.DrawModifierNode
import de.fabmax.kool.modules.compose.composables.DrawScope
import me.dvyy.compose.mini.modifier.Modifier
import me.dvyy.compose.mini.modifier.ModifierNodeElement

//
//fun interface DrawModifier : Modifier.Element {
//    fun draw(scope: DrawScope)
//}

fun Modifier.drawBehind(
    onDraw: DrawScope.() -> Unit,
): Modifier = this then DrawBehindElement(onDraw)

private data class DrawBehindElement(
    val onDraw: DrawScope.() -> Unit,
) : ModifierNodeElement<DrawBehindNode>() {
    override fun create(): DrawBehindNode = DrawBehindNode(onDraw = onDraw)

    override fun update(node: DrawBehindNode) {
        node.onDraw = onDraw
    }

    override fun toString() = "DrawBehind"
}

private class DrawBehindNode(
    var onDraw: DrawScope.() -> Unit,
) : DrawModifierNode, Modifier.Node() {
    override fun DrawScope.draw() {
        onDraw()
        drawContent() //TODO split into ContentDrawScope and DrawScope
    }
}