package de.fabmax.kool.modules.compose.composables.rendering

import androidx.compose.runtime.Composable
import de.fabmax.kool.modules.compose.LocalUiSurface
import de.fabmax.kool.modules.compose.composables.layout.Box
import de.fabmax.kool.modules.compose.modifiers.edit
import de.fabmax.kool.modules.ui2.AttributedTextModifier
import de.fabmax.kool.modules.ui2.TextLine
import me.dvyy.compose.mini.modifier.Modifier

@Composable
fun AttributedText(
    text: TextLine,
    modifier: Modifier = Modifier.Companion,
) {
    val surface = LocalUiSurface.current
    Box(
        modifier
            .edit<AttributedTextModifier> { it.text = text }
//            .hoverListener(textNode)
//            .dragListener(textNode)
    ) {}
}