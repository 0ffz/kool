package de.fabmax.kool.modules.compose.composables.toolkit

import androidx.compose.runtime.Composable
import de.fabmax.kool.input.KeyboardInput
import de.fabmax.kool.modules.compose.LocalUiSurface
import de.fabmax.kool.modules.compose.composables.rendering.Text
import de.fabmax.kool.modules.compose.modifiers.onKeyEvent
import me.dvyy.compose.mini.modifier.Modifier

@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    onSubmit: (String) -> Unit = {},
//    font: Font = LocalSizes.current.normalText,
    modifier: Modifier = Modifier,
) {
    val surface = LocalUiSurface.current
//    val textFieldNode = remember { TextFieldNode(null, surface) }

//    CompatUiScope {
//        if (textFieldNode.isFocused.use()) {
//            surface.onEachFrame { ctx ->
//                textFieldNode.updateCaretBlinkState(ctx)
//            }
//        }
//    }
    Text(value, Modifier.onKeyEvent { event ->
        var changed = value
        if (event.isCharTyped) changed += event.typedChar
        else if (event.isPressed) {
            when (event.keyCode) {
                KeyboardInput.KEY_BACKSPACE -> {
                    changed = changed.dropLast(1)
                }

                else -> {}
            }
        }
        onValueChange(changed)
        true
    })
//    Layout(
//        modifier
////            .onClick { textFieldNode.onClick(it) }
////            .hoverListener(textFieldNode)
////            .dragListener(textFieldNode)
////            .edit<TextFieldModifier> { it.onChange { onValueChange(it) } }
////            .edit<TextFieldModifier> { it.text(value) }
////            .edit<TextFieldModifier> { it.onEnterPressed { onSubmit(it) } }
////            .edit<TextFieldModifier> { it.font(font) }
//    ) {}
}
