package de.fabmax.kool.modules.compose.composables.toolkit

import androidx.compose.runtime.Composable
import de.fabmax.kool.modules.compose.LocalUiSurface
import de.fabmax.kool.modules.compose.composables.layout.Box
import me.dvyy.compose.mini.modifier.Modifier

@Composable
fun Slider(
    value: Float,
    onValueChange: (Float) -> Unit,
    range: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
) {
    val surface = LocalUiSurface.current
//    val slider = remember { SliderNode(null, surface) }
    Box(
        modifier = modifier
//            .edit<SliderModifier> { it.onChange { onValueChange(it) } }
//            .edit<SliderModifier> { it.value(value) }
//            .edit<SliderModifier> { it.minValue(range.start).maxValue(range.endInclusive) }
//            .dragListener(slider)
    ) {}
}
