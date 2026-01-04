package de.fabmax.kool.modules.compose.composables.toolkit

import androidx.compose.runtime.Composable
import de.fabmax.kool.modules.compose.LocalColors
import de.fabmax.kool.modules.compose.LocalSizes
import de.fabmax.kool.modules.compose.composables.layout.Box
import de.fabmax.kool.modules.compose.composables.layout.Column
import de.fabmax.kool.modules.compose.composables.layout.Popup
import de.fabmax.kool.modules.compose.modifiers.background
import de.fabmax.kool.modules.compose.modifiers.clickable
import de.fabmax.kool.modules.compose.modifiers.padding
import de.fabmax.kool.modules.ui2.RoundRectBackground
import de.fabmax.kool.modules.ui2.dp
import me.dvyy.compose.mini.modifier.Modifier

@Composable
fun DropdownMenu(
    expanded: Boolean,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
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
