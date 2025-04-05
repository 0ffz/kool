package de.fabmax.kool.modules.compose.composables

import androidx.compose.runtime.Composable
import de.fabmax.kool.modules.compose.Layout
import de.fabmax.kool.modules.compose.modifiers.Modifier
import de.fabmax.kool.modules.compose.modifiers.text
import de.fabmax.kool.modules.ui2.TextNode

@Composable
fun Text(text: String, modifier: Modifier = Modifier.Companion) {
    Layout(::TextNode, modifier.text(text))
}
