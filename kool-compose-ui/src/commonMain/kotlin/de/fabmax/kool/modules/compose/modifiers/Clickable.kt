package de.fabmax.kool.modules.compose.modifiers

import androidx.compose.runtime.getValue
import de.fabmax.kool.math.Easing
import de.fabmax.kool.math.MutableVec2f
import de.fabmax.kool.modules.compose.composables.DrawModifierNode
import de.fabmax.kool.modules.compose.composables.DrawScope
import de.fabmax.kool.modules.compose.state.asComposeState
import de.fabmax.kool.modules.ui2.FloatAnimator
import de.fabmax.kool.modules.ui2.PointerEvent
import de.fabmax.kool.util.Color
import de.fabmax.kool.util.Time
import me.dvyy.compose.mini.modifier.Modifier
import me.dvyy.compose.mini.modifier.ModifierNodeElement

/**
 * Calls [onClick] when this element is clicked, also adding a ripple effect.
 */
fun Modifier.clickable(
//    hoverBackground: UiRenderer<UiNode> = RectBackground(Color.WHITE.withAlpha(0.2f)),
    onClick: (PointerEvent) -> Unit,
) = then(ClickableElement(onClick))

private data class ClickableElement(val onClick: (PointerEvent) -> Unit) : ModifierNodeElement<ClickableNode>() {
    override fun create(): ClickableNode = ClickableNode(onClick)
    override fun update(node: ClickableNode) {
        node.clickAction = onClick
    }
}

//TODO hover
private class ClickableNode(
    var clickAction: (PointerEvent) -> Unit,
) : Modifier.Node(), ClickModifierNode, DrawModifierNode {
    val clickAnimator = FloatAnimator(0.3f, Easing.linear)
    val size by clickAnimator.animatable.asComposeState()
    val clickPos = MutableVec2f()

    override fun onClick(event: PointerEvent): Boolean {
        clickAnimator.start(1f, startFrom = 0f)
        clickPos.set(event.position)
        clickAction(event)
        return true
    }

    override fun DrawScope.draw() {
        if (clickAnimator.isActive) {
            clickAnimator.update(Time.deltaT)
            surface.getMeshLayer(0).uiPrimitives.circle(
                x + clickPos.x, y + clickPos.y,
                size * 128,
                clipBounds,
                Color.WHITE.withAlpha(0.7f - size * 0.5f)
            )
        }
        drawContent()
    }
}
