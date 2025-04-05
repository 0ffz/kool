package de.fabmax.kool.modules.compose.composables

import androidx.compose.runtime.Composable
import de.fabmax.kool.modules.compose.Layout
import de.fabmax.kool.modules.compose.modifiers.Modifier
import de.fabmax.kool.modules.ui2.BoxNode

@Composable
fun Box(modifier: Modifier = Modifier.Companion, content: @Composable () -> Unit) {
    Layout(::BoxNode, modifier) {
        content()
    }
}
