package de.fabmax.kool.modules.compose.composables.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import de.fabmax.kool.modules.compose.composables.Layout
import me.dvyy.compose.mini.layout.ColumnMeasurePolicy
import me.dvyy.compose.mini.layout.jetpack.Alignment
import me.dvyy.compose.mini.layout.jetpack.Arrangement
import me.dvyy.compose.mini.modifier.Modifier

@Composable
fun Column(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable () -> Unit,
) {
    val measurePolicy = remember(verticalArrangement, horizontalAlignment) {
        ColumnMeasurePolicy(
            verticalArrangement,
            horizontalAlignment
        )
    }
    Layout(measurePolicy, modifier) {
        content()
    }
}
