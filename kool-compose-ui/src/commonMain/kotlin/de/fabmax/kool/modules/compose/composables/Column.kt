package de.fabmax.kool.modules.compose.composables

import androidx.compose.runtime.Composable
import de.fabmax.kool.modules.compose.Layout
import de.fabmax.kool.modules.compose.modifiers.Modifier
import de.fabmax.kool.modules.compose.modifiers.layout
import de.fabmax.kool.modules.ui2.ColumnLayout
import de.fabmax.kool.modules.ui2.RowNode

@Composable
fun Column(modifier: Modifier = Modifier.Companion, content: @Composable () -> Unit) {
    Layout(::RowNode, modifier.layout(ColumnLayout)) {
        content()
    }
}
