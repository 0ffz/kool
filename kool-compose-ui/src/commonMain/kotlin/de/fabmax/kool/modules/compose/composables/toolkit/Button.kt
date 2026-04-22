package de.fabmax.kool.modules.compose.composables.toolkit

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import de.fabmax.kool.modules.compose.Colors
import de.fabmax.kool.modules.compose.composables.layout.Box
import de.fabmax.kool.modules.compose.modifiers.clickable
import de.fabmax.kool.modules.compose.modifiers.drawBehind
import de.fabmax.kool.util.Color
import me.dvyy.compose.mini.layout.jetpack.Alignment
import me.dvyy.compose.mini.layout.modifiers.padding
import me.dvyy.compose.mini.modifier.Modifier

@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Colors.secondaryVariant,
    content: @Composable () -> Unit,
) = Box(
    modifier.drawBehind {
        val cornerRadius = 4.dp.roundToPx().toFloat()
        surface.getMeshLayer(0).uiPrimitives.roundRect(x, y, width, height, cornerRadius, clipBounds, color)
//            TODO (RoundRectBackground(color, 4.dp))
    }
        .clickable { onClick() }
        //TODO use Sizes.gap, Sizes.smallGap when we migrate it to dp
        .padding(horizontal = 4.dp, vertical = 4.dp),
    contentAlignment = Alignment.Center
) {
    content()
}
