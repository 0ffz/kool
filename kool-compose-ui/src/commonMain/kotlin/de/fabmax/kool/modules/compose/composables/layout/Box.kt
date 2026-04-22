package de.fabmax.kool.modules.compose.composables.layout

import androidx.compose.runtime.Composable
import de.fabmax.kool.modules.compose.composables.Layout
import me.dvyy.compose.mini.layout.jetpack.Alignment
import me.dvyy.compose.mini.layout.jetpack.BoxScope
import me.dvyy.compose.mini.layout.jetpack.BoxScopeInstance
import me.dvyy.compose.mini.layout.jetpack.rememberBoxMeasurePolicy
import me.dvyy.compose.mini.modifier.Modifier

@Composable
fun Box(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.() -> Unit,
) {
    val measurePolicy = rememberBoxMeasurePolicy(alignment = contentAlignment, false)
    Layout(measurePolicy, modifier) {
        BoxScopeInstance.content()
    }
}
