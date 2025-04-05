package de.fabmax.kool.modules.compose.composables

import androidx.compose.runtime.Composable
import de.fabmax.kool.modules.compose.Layout
import de.fabmax.kool.modules.compose.modifiers.Modifier
import de.fabmax.kool.modules.compose.modifiers.layout
import de.fabmax.kool.modules.ui2.RowLayout
import de.fabmax.kool.modules.ui2.RowNode


@Composable
fun Row(modifier: Modifier = Modifier.Companion, content: @Composable () -> Unit) {
    Layout(::RowNode, modifier.layout(RowLayout)) {
        content()
    }
}

