package de.fabmax.kool.modules.compose.composables.toolkit

import androidx.compose.runtime.*
import de.fabmax.kool.modules.compose.Colors
import de.fabmax.kool.modules.compose.Sizes
import de.fabmax.kool.modules.compose.composables.layout.Box
import de.fabmax.kool.modules.compose.modifiers.*
import de.fabmax.kool.modules.ui2.RoundRectBackground
import de.fabmax.kool.modules.ui2.dp

@Composable
fun Button(onClick: () -> Unit, modifier: Modifier = Modifier.Companion, content: @Composable () -> Unit) {
    var isHovered by remember { mutableStateOf(false) }
    val color = if (isHovered) Colors.secondary else Colors.secondaryVariant

    Box(
        modifier.background(RoundRectBackground(color, 4.dp))
            .onEnter { isHovered = true }
            .onExit { isHovered = false }
            .padding(horizontal = Sizes.gap, vertical = Sizes.smallGap)
            .clickable { onClick() }
    ) {
        content()
    }
}
