package de.fabmax.kool.modules.compose.composables.toolkit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import de.fabmax.kool.modules.compose.Layout
import de.fabmax.kool.modules.compose.LocalUiSurface
import de.fabmax.kool.modules.compose.modifiers.*
import de.fabmax.kool.modules.ui2.*

@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    onSubmit: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val surface = LocalUiSurface.current
    val textFieldNode = remember { TextFieldNode(null, surface) }
    Layout(
        { _, _ -> textFieldNode }, modifier
            .onClick { textFieldNode.onClick(it) }
            .hoverListener(textFieldNode)
            .dragListener(textFieldNode)
            .edit<TextFieldModifier> { it.onChange { onValueChange(it) } }
            .edit<TextFieldModifier> { it.text(value) }
            .edit<TextFieldModifier> { it.onEnterPressed { onSubmit(it) } }
    )
}
