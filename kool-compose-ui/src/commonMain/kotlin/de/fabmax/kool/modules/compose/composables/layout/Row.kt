package de.fabmax.kool.modules.compose.composables.layout

import androidx.compose.runtime.Composable
import de.fabmax.kool.modules.compose.composables.Layout
import me.dvyy.compose.mini.layout.jetpack.*
import me.dvyy.compose.mini.modifier.Modifier

@Composable
fun Row(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    content: @Composable RowScope.() -> Unit,
) {
    val measurePolicy = rowMeasurePolicy(horizontalArrangement, verticalAlignment)
    Layout(measurePolicy, modifier) {
        RowScopeInstance.content()
    }
}
