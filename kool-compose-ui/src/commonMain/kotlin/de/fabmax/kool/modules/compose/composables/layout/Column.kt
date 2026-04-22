package de.fabmax.kool.modules.compose.composables.layout

import androidx.compose.runtime.Composable
import de.fabmax.kool.modules.compose.composables.Layout
import me.dvyy.compose.mini.layout.jetpack.*
import me.dvyy.compose.mini.modifier.Modifier

@Composable
fun Column(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit,
) {
    val measurePolicy = columnMeasurePolicy(verticalArrangement, horizontalAlignment)
    Layout(measurePolicy, modifier) {
        ColumnScopeInstance.content()
    }
}
