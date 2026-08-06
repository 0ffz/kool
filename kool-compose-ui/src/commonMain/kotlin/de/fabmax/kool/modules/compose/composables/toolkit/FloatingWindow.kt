package de.fabmax.kool.modules.compose.composables.toolkit

import androidx.compose.runtime.*
import de.fabmax.kool.math.Vec2f
import de.fabmax.kool.modules.compose.LocalColors
import de.fabmax.kool.modules.compose.LocalSizes
import de.fabmax.kool.modules.compose.composables.layout.Column
import de.fabmax.kool.modules.compose.composables.layout.Popup
import de.fabmax.kool.modules.ui2.dp
import me.dvyy.compose.mini.modifier.Modifier

@Composable
fun FloatingWindow(
    title: String,
    startOffset: Vec2f = Vec2f.ZERO,
    modifier: Modifier = Modifier.Companion,
    layer: Int,
    content: @Composable () -> Unit,
) {
    var x by remember { mutableStateOf(startOffset.x.dp) }
    var y by remember { mutableStateOf(startOffset.y.dp) }
    Popup(offset = Vec2f(x.value, y.value), layerOffset = layer, relativeToParent = false) {
        val colors = LocalColors.current
        val sizes = LocalSizes.current
        Column(
            Modifier
//                .drawBehind(RoundRectBackground(colors.background, sizes.smallGap))
//                .border(RoundRectBorder(colors.backgroundVariant, sizes.smallGap, sizes.borderWidth))
        ) {
//            Row(
//                Modifier.fillMaxWidth()
//                    .background(TitleBarBackground(colors.backgroundVariant, sizes.smallGap.value, false))
//                    .padding(sizes.smallGap)
//                    .dragListener(object : Draggable {
//                        override fun onDrag(ev: PointerEvent) {
//                            x += Dp.fromPx(ev.pointer.delta.x)
//                            y += Dp.fromPx(ev.pointer.delta.y)
//                        }
//                    })
//            ) {
//                Text(title)
//            }
//            Column(modifier.padding(sizes.smallGap)) {
//                content()
//            }
        }
    }
}