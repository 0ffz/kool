package de.fabmax.kool.modules.compose.composables.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import de.fabmax.kool.modules.compose.composables.Layout
import me.dvyy.compose.mini.layout.RowMeasurePolicy
import me.dvyy.compose.mini.layout.jetpack.Alignment
import me.dvyy.compose.mini.layout.jetpack.Arrangement
import me.dvyy.compose.mini.modifier.Modifier

@Composable
fun Row(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    content: @Composable () -> Unit,
) {
    val measurePolicy = remember(horizontalArrangement, verticalAlignment) {
        RowMeasurePolicy(
            horizontalArrangement,
            verticalAlignment
        )
    }
    Layout(measurePolicy, modifier) {
        content()
    }
}
