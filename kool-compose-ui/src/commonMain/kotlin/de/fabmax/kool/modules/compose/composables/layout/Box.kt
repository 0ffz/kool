package de.fabmax.kool.modules.compose.composables.layout

import androidx.compose.runtime.Composable
import de.fabmax.kool.modules.compose.Layout
import de.fabmax.kool.modules.compose.composables.ComposableUiNode
import de.fabmax.kool.modules.compose.modifiers.Modifier

@Composable
fun Box(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Layout(::ComposableUiNode, modifier) {
        content()
    }
}
