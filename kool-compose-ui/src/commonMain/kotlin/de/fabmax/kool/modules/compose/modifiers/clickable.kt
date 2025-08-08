package de.fabmax.kool.modules.compose.modifiers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import de.fabmax.kool.math.MutableVec2f
import de.fabmax.kool.modules.compose.Colors
import de.fabmax.kool.modules.compose.animation.LaunchAnimation
import de.fabmax.kool.modules.compose.animation.collectAsState
import de.fabmax.kool.modules.ui2.AnimatedFloat
import de.fabmax.kool.modules.ui2.PointerEvent
import de.fabmax.kool.modules.ui2.RectBackground
import de.fabmax.kool.modules.ui2.UiNode
import de.fabmax.kool.modules.ui2.UiRenderer
import de.fabmax.kool.scene.geometry.Shape
import de.fabmax.kool.util.Color

/**
 * Calls [onClick] when this element is clicked, also adding a ripple effect.
 */
fun Modifier.clickable(
    hoverBackground: UiRenderer<UiNode> = RectBackground(Color.WHITE.withAlpha(0.2f)),
    onClick: (PointerEvent) -> Unit,
) = composed {
    val animator = remember { AnimatedFloat(0.3f) }
    val clickPos = remember { MutableVec2f() }
    var isHovered by remember { mutableStateOf(false) }
    val hovered by rememberUpdatedState(isHovered)

    val animatedRippleProgress by animator.collectAsState()
    LaunchAnimation(animator)
    animatedRippleProgress
    onClick { clickPos.set(it.position); animator.start(); onClick(it) }
        .draw {
            // Click ripple animation
            if (animator.isActive) getUiPrimitives().localCircle(
                clickPos.x, clickPos.y,
                animatedRippleProgress * 128.dp.px,
                Color.WHITE.withAlpha(0.7f - animatedRippleProgress * 0.5f)
            )
        }.draw {
            // Hover overlay
            if (hovered) hoverBackground.renderUi(this)
        }
        .onEnter { isHovered = true }
        .onExit { isHovered = false }

}
