package de.fabmax.kool.modules.compose.composables.rendering

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import de.fabmax.kool.modules.compose.Layout
import de.fabmax.kool.modules.compose.LocalContentColor
import de.fabmax.kool.modules.compose.LocalTextStyle
import de.fabmax.kool.modules.compose.modifiers.Modifier
import de.fabmax.kool.modules.compose.modifiers.edit
import de.fabmax.kool.modules.ui2.TextModifier
import de.fabmax.kool.modules.ui2.TextNode
import de.fabmax.kool.modules.ui2.isWrapText
import de.fabmax.kool.modules.ui2.text
import de.fabmax.kool.modules.ui2.textColor
import de.fabmax.kool.util.Color

@Composable
fun Text(
    text: String,
    modifier: Modifier = Modifier,
    color: Color? = null,
    softWrap: Boolean = false,
    style: TextStyle = LocalTextStyle.current,
) {
    val textColor = color ?: style.color ?: LocalContentColor.current
    Layout(
        ::TextNode, modifier
            .text(text)
            .textColor(textColor)
            .isWrapText(softWrap)
    )
}

@Stable
private fun Modifier.text(text: String) = edit<TextModifier> { it.text(text) }

@Stable
private fun Modifier.textColor(color: Color) = edit<TextModifier> { it.textColor(color) }

@Stable
private fun Modifier.isWrapText(enabled: Boolean) = edit<TextModifier> { it.isWrapText(enabled) }

data class TextStyle(
    val color: Color? = null,
) {
    companion object {
        val Default = TextStyle()
    }
}
