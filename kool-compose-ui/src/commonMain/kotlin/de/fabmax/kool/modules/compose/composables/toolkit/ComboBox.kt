package de.fabmax.kool.modules.compose.composables.toolkit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import de.fabmax.kool.modules.compose.LocalColors
import de.fabmax.kool.modules.compose.LocalSizes
import de.fabmax.kool.modules.compose.LocalUiSurface
import de.fabmax.kool.modules.compose.composables.layout.Box
import de.fabmax.kool.modules.compose.composables.layout.Column
import de.fabmax.kool.modules.compose.composables.layout.Popup
import de.fabmax.kool.modules.compose.modifiers.background
import de.fabmax.kool.modules.compose.modifiers.clickable
import de.fabmax.kool.modules.compose.modifiers.padding
import de.fabmax.kool.modules.ui2.ComboBoxNode
import de.fabmax.kool.modules.ui2.PointerEvent
import de.fabmax.kool.modules.ui2.RoundRectBackground
import de.fabmax.kool.modules.ui2.dp
import me.dvyy.compose.minimal.modifier.Modifier

@Composable
fun DropdownMenu(
    expanded: Boolean,
    modifier: Modifier = Modifier,
    onDismissRequest: (PointerEvent) -> Unit = {},
    content: @Composable () -> Unit,
) {
    if (expanded) Popup(
        relativeToParent = true,
        onDismissRequest = onDismissRequest,
        modifier = modifier.background(RoundRectBackground(LocalColors.current.background, 4.dp))
    ) {
        Column(modifier) {
            content()
        }
    }
}

@Composable
fun DropdownMenuItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    text: @Composable () -> Unit,
) = Box(modifier.padding(LocalSizes.current.smallGap).clickable { onClick() }) {
    text()
}

@Composable
fun ComboBox(
    selected: Int,
    items: List<Any>,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val surface = LocalUiSurface.current
    val sizes = LocalSizes.current
    val comboBox = remember { ComboBoxNode(null, surface) }
    TODO("Implement with composable popup system")
//    Layout(
//        { _, _ -> comboBox },
//        modifier = modifier
//            .padding(horizontal = sizes.gap, vertical = sizes.smallGap)
//            .hoverListener(comboBox)
//            .onClick { comboBox.onClick(it) }
//            .edit<ComboBoxModifier> { it.selectedIndex(selected) }
//            .edit<ComboBoxModifier> { it.items(items) }
//            .edit<ComboBoxModifier> { it.onItemSelected { onItemSelected(it) } }
//    )
}
