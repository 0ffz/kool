package de.fabmax.kool.modules.compose.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import de.fabmax.kool.math.MutableVec2f
import de.fabmax.kool.modules.compose.Colors
import de.fabmax.kool.modules.compose.animation.animateFloatAsState
import de.fabmax.kool.modules.compose.modifiers.Modifier
import de.fabmax.kool.modules.compose.modifiers.background
import de.fabmax.kool.modules.compose.modifiers.onClick
import de.fabmax.kool.modules.compose.modifiers.onEnter
import de.fabmax.kool.modules.compose.modifiers.onExit
import de.fabmax.kool.modules.compose.modifiers.onRender
import de.fabmax.kool.modules.ui2.Colors
import de.fabmax.kool.modules.ui2.Dp
import de.fabmax.kool.modules.ui2.RoundRectBackground
import de.fabmax.kool.util.Color
import de.fabmax.kool.util.MdColor

@Composable
fun Button(modifier: Modifier = Modifier.Companion, content: @Composable () -> Unit) {
    var isHovered by remember { mutableStateOf(false) }
    var rippleProgress by remember { mutableStateOf(0f) }
    val animatedRippleProgress by animateFloatAsState(rippleProgress)
    val clickPos = remember { MutableVec2f() }
    val color = if (isHovered) Colors.secondary else Colors.secondaryVariant
//    println("recomposing with $animatedRippleProgress")
    animatedRippleProgress
    Box(
        modifier.background(RoundRectBackground(color, Dp(4f)))
            .onEnter { isHovered = true }
            .onExit { isHovered = false }
            .onClick { clickPos.set(it.position); rippleProgress = 1f }
            .onRender {
                if (animatedRippleProgress != 0f) getUiPrimitives().localCircle(
                    clickPos.x, clickPos.y,
                    animatedRippleProgress * 128.dp.px,
                    Color.Companion.WHITE.withAlpha(0.7f - animatedRippleProgress * 0.5f)
                )
                if(animatedRippleProgress == 1f) rippleProgress = 0f
            }
    ) {
        content()
    }
}
