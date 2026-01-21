package de.fabmax.kool.modules.compose.composables.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import de.fabmax.kool.modules.compose.composables.Layout
import me.dvyy.compose.mini.layout.BoxMeasurePolicy
import me.dvyy.compose.mini.layout.jetpack.Alignment
import me.dvyy.compose.mini.modifier.Modifier

@Composable
fun Box(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable () -> Unit,
) {
    val measurePolicy = remember(contentAlignment) { BoxMeasurePolicy(contentAlignment) }
    Layout(measurePolicy, modifier) {
        content()
    }
}
