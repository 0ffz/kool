package de.fabmax.kool.modules.compose.modifiers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import de.fabmax.kool.math.Easing
import de.fabmax.kool.math.MutableVec2f
import de.fabmax.kool.modules.compose.Colors
import de.fabmax.kool.modules.compose.animation.LaunchAnimation
import de.fabmax.kool.modules.compose.animation.collectAsState
import de.fabmax.kool.modules.ui2.FloatAnimator
import de.fabmax.kool.modules.ui2.PointerEvent
import de.fabmax.kool.modules.ui2.RectBackground
import de.fabmax.kool.util.Color
import de.fabmax.kool.util.Time

/**
 * Calls [onClick] when this element is clicked, also adding a ripple effect.
 */
fun Modifier.clickable(onClick: (PointerEvent) -> Unit) = composed {
    val animator = remember { FloatAnimator(0.3f, Easing.linear) }
    val clickPos = remember { MutableVec2f() }
    var isHovered by remember { mutableStateOf(false) }
    val color = if (isHovered) Colors.secondary else Colors.secondaryVariant

    LaunchAnimation(animator)
    val animatedRippleProgress by animator.animatable.collectAsState()
    animatedRippleProgress
    this.onClick {
        clickPos.set(it.position)
        animator.start(1f, startFrom = 0f)
        animator.update(Time.deltaT)
        onClick(it)
    }.draw {
        if (animator.isActive) getUiPrimitives().localCircle(
            clickPos.x, clickPos.y,
            animator.value * 128.dp.px,
            Color.WHITE.withAlpha(0.7f - animator.value * 0.5f)
        )
    }.draw { if (isHovered) RectBackground(Color.WHITE.withAlpha(0.2f)).renderUi(this) }
        .onEnter { isHovered = true }
        .onExit { isHovered = false }

}
