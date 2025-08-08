package de.fabmax.kool.modules.compose.composables.layout

import androidx.compose.runtime.Composable
import de.fabmax.kool.modules.compose.Layout
import de.fabmax.kool.modules.compose.composables.ComposableUiNode
import de.fabmax.kool.modules.compose.modifiers.Modifier
import de.fabmax.kool.modules.compose.modifiers.layout
import de.fabmax.kool.modules.ui2.RowLayout

@Composable
fun Row(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Layout(::ComposableUiNode, modifier.layout(RowLayout)) {
        content()
    }
}
