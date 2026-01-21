package de.fabmax.kool.modules.compose.composables.rendering

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import de.fabmax.kool.modules.compose.LocalContentColor
import de.fabmax.kool.modules.compose.LocalSizes
import de.fabmax.kool.modules.compose.LocalTextStyle
import de.fabmax.kool.modules.compose.composables.Layout
import de.fabmax.kool.modules.compose.modifiers.draw
import de.fabmax.kool.modules.compose.modifiers.edit
import de.fabmax.kool.modules.ui2.*
import de.fabmax.kool.modules.ui2.UiNode.Companion.NO_CLIP
import de.fabmax.kool.scene.geometry.MeshBuilder
import de.fabmax.kool.scene.geometry.TextProps
import de.fabmax.kool.util.Color
import de.fabmax.kool.util.Font
import de.fabmax.kool.util.MutableStructBufferView
import de.fabmax.kool.util.set
import me.dvyy.compose.mini.layout.MeasurePolicy
import me.dvyy.compose.mini.layout.MeasureResult
import me.dvyy.compose.mini.modifier.Modifier

@PublishedApi
internal val setBoundsTextVertexNoClip: MutableStructBufferView<UiTextVertexLayout>.(UiTextVertexLayout) -> Unit = {
    it.clip.set(NO_CLIP)
}
@Composable
fun Text(
    text: String,
    modifier: Modifier = Modifier,
    font: Font = LocalSizes.current.normalText,
    fontSize: Float? = null,
    color: Color? = null,
    softWrap: Boolean = false,
    style: TextStyle = LocalTextStyle.current,
) {
    val font = font.let {
        if (fontSize != null) it.derive(fontSize)
        else it
    }
    val textColor = color ?: style.color ?: LocalContentColor.current
    val props = TextProps(Font.DEFAULT_FONT).apply {
        this.text = text
        isYAxisUp = false
    }
    val textMetrics = remember(font, text) { font.textDimensions(text) }
    Layout(
        MeasurePolicy { measurables, constraints ->
//            val measured = measurables.map { it.measure(constraints) }
            MeasureResult(textMetrics.width.toInt(), textMetrics.height.toInt()) {
            }
        },
        modifier
            .draw {
                getMeshLayer(0).getTextBuilder(font).withColor(textColor) {
//                    rotate(180f, Vec3f.Z_AXIS)
                    translate(it.x.toFloat(), it.y.toFloat(), 0f)
                    translate(0f, textMetrics.yBaseline, 0f)
                    (this as MeshBuilder<UiTextVertexLayout>).vertexCustomizer = setBoundsTextVertexNoClip
                    text(props)
                }
            }
//            .text(text)
//            .textColor(textColor)
//            .isWrapText(softWrap)
//            .font(font)
    ) {}
}

@Stable
private fun Modifier.font(font: Font) = edit<TextModifier> { it.font(font) }

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
