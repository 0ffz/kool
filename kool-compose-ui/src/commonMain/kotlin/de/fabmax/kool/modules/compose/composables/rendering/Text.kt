package de.fabmax.kool.modules.compose.composables.rendering

import androidx.compose.runtime.Composable
import de.fabmax.kool.modules.compose.composables.Layout
import de.fabmax.kool.modules.compose.modifiers.text
import de.fabmax.kool.modules.ui2.TextNode
import me.dvyy.compose.minimal.modifier.Modifier

@Composable
fun Text(text: String, modifier: Modifier = Modifier) {
    Layout(::TextNode, modifier.text(text))
}
