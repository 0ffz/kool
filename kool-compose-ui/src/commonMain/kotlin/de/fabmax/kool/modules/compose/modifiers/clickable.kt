package de.fabmax.kool.modules.compose.modifiers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import de.fabmax.kool.math.MutableVec2f
import de.fabmax.kool.modules.compose.animation.LaunchAnimation
import de.fabmax.kool.modules.compose.animation.collectAsState
import de.fabmax.kool.modules.ui2.AnimatedFloat
import de.fabmax.kool.modules.ui2.PointerEvent
import de.fabmax.kool.util.Color

/**
 * Calls [onClick] when this element is clicked, also adding a ripple effect.
 */
fun Modifier.clickable(onClick: (PointerEvent) -> Unit) = composed {
    val animator = remember { AnimatedFloat(0.3f) }
    val clickPos = remember { MutableVec2f() }
    val animatedRippleProgress by animator.collectAsState()
    LaunchAnimation(animator)
    animatedRippleProgress
    this.onClick { clickPos.set(it.position); animator.start(); onClick(it) }.draw {
        if (animator.isActive) getUiPrimitives().localCircle(
            clickPos.x, clickPos.y,
            animatedRippleProgress * 128.dp.px,
            Color.Companion.WHITE.withAlpha(0.7f - animatedRippleProgress * 0.5f)
        )
    }
}
