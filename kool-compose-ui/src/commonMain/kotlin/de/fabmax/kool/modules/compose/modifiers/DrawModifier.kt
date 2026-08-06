package de.fabmax.kool.modules.compose.modifiers

import androidx.compose.runtime.Stable
import de.fabmax.kool.modules.compose.composables.ContentDrawScope
import de.fabmax.kool.modules.compose.composables.DrawModifierNode
import de.fabmax.kool.modules.compose.composables.DrawScope
import de.fabmax.kool.util.Color
import me.dvyy.compose.mini.modifier.Modifier
import me.dvyy.compose.mini.modifier.ModifierNodeElement

//
//fun interface DrawModifier : Modifier.Element {
//    fun draw(scope: DrawScope)
//}

fun Modifier.drawBehind(
    onDraw: DrawScope.() -> Unit,
): Modifier = this then DrawBehindElement(onDraw)

fun Modifier.drawWithContent(
    onDraw: ContentDrawScope.() -> Unit,
): Modifier = this then DrawWithContentElement(onDraw)

@Stable
fun Modifier.background(color: Color): Modifier { //TODO shape
    return drawBehind {
        surface.getMeshLayer(0).uiPrimitives.rect(x, y, width, height, clipBounds, color)
    }
}
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
    override fun ContentDrawScope.draw() {
        onDraw()
        drawContent()
    }
}

private data class DrawWithContentElement(
    val onDraw: ContentDrawScope.() -> Unit,
) : ModifierNodeElement<DrawWithContentNode>() {
    override fun create(): DrawWithContentNode = DrawWithContentNode(onDraw = onDraw)

    override fun update(node: DrawWithContentNode) {
        node.onDraw = onDraw
    }

    override fun toString() = "DrawWithContentNode"
}

private class DrawWithContentNode(
    var onDraw: ContentDrawScope.() -> Unit,
) : DrawModifierNode, Modifier.Node() {
    override fun ContentDrawScope.draw() {
        onDraw()
    }
}