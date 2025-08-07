package de.fabmax.kool.modules.compose.composables.toolkit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import de.fabmax.kool.modules.compose.Layout
import de.fabmax.kool.modules.compose.LocalSizes
import de.fabmax.kool.modules.compose.LocalUiSurface
import de.fabmax.kool.modules.compose.modifiers.Modifier
import de.fabmax.kool.modules.compose.modifiers.edit
import de.fabmax.kool.modules.compose.modifiers.hoverListener
import de.fabmax.kool.modules.compose.modifiers.onClick
import de.fabmax.kool.modules.compose.modifiers.padding
import de.fabmax.kool.modules.ui2.*

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
