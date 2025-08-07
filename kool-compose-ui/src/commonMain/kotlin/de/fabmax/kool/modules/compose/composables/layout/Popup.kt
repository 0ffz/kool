package de.fabmax.kool.modules.compose.composables.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import de.fabmax.kool.math.Vec2f
import de.fabmax.kool.modules.compose.modifiers.Modifier
import de.fabmax.kool.modules.compose.modifiers.align
import de.fabmax.kool.modules.compose.modifiers.margin
import de.fabmax.kool.modules.compose.modifiers.onPositioned
import de.fabmax.kool.modules.compose.surface.layers.Content
import de.fabmax.kool.modules.compose.surface.layers.rememberComposeSceneLayer
import de.fabmax.kool.modules.ui2.AlignmentX
import de.fabmax.kool.modules.ui2.AlignmentY
import de.fabmax.kool.modules.ui2.dp

@Composable
private fun Popup(
    content: @Composable () -> Unit,
) {
    val layer = rememberComposeSceneLayer()
    layer.Content(content)
}

/**
 * A separate layer in this composition, positioned either relative to the parent node, or the root node.
 */
@Composable
fun Popup(
    offset: Vec2f = Vec2f.ZERO,
    relativeToParent: Boolean = true,
    alignmentX: AlignmentX = AlignmentX.Start,
    alignmentY: AlignmentY = AlignmentY.Top,
    content: @Composable () -> Unit,
) {
    var parentPosition by remember { mutableStateOf(Vec2f.ZERO) }
    Box((if (relativeToParent) Modifier.onPositioned {
        parentPosition = Vec2f(it.leftPx, it.bottomPx)
    } else Modifier.Companion)) {}

    Popup {
        Box(
            Modifier.margin(
                start = offset.x.dp + parentPosition.x.dp,
                top = offset.y.dp + parentPosition.y.dp,
                end = 0.dp,
                bottom = 0.dp
            ).align(alignmentX, alignmentY)
        ) {
            content()
        }
    }
}
