package de.fabmax.kool.modules.compose.composables.rendering

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.constrain
import de.fabmax.kool.math.Vec3f
import de.fabmax.kool.modules.compose.LocalContentColor
import de.fabmax.kool.modules.compose.LocalSizes
import de.fabmax.kool.modules.compose.LocalTextStyle
import de.fabmax.kool.modules.compose.composables.Layout
import de.fabmax.kool.modules.compose.modifiers.drawBehind
import de.fabmax.kool.modules.compose.modifiers.edit
import de.fabmax.kool.modules.ui2.*
import de.fabmax.kool.modules.ui2.UiNode.Companion.NO_CLIP
import de.fabmax.kool.scene.geometry.MeshBuilder
import de.fabmax.kool.scene.geometry.TextProps
import de.fabmax.kool.util.Color
import de.fabmax.kool.util.Font
import de.fabmax.kool.util.MutableStructBufferView
import de.fabmax.kool.util.set
import me.dvyy.compose.mini.layout.jetpack.Measurable
import me.dvyy.compose.mini.layout.jetpack.MeasurePolicy
import me.dvyy.compose.mini.layout.jetpack.MeasureResult
import me.dvyy.compose.mini.layout.jetpack.MeasureScope
import me.dvyy.compose.mini.modifier.Modifier

@PublishedApi
internal val setBoundsTextVertexNoClip: MutableStructBufferView<UiTextVertexLayout>.(UiTextVertexLayout) -> Unit = {
    it.clip.set(NO_CLIP)
}

class TextMeasurePolicy(
    val text: String,
    val font: Font,
    val softWrap: Boolean,
) : MeasurePolicy {
    private var wrappedWidth: Int = -1
    private var wrappedText: String = text
    var textMetrics = font.textDimensions(wrappedText)
    var props = TextProps(font).apply {
        text = this@TextMeasurePolicy.text
        isYAxisUp = false
    }

    override fun MeasureScope.measure(
        measurables: List<Measurable>,
        constraints: Constraints,
    ): MeasureResult {
        if (constraints.maxWidth != wrappedWidth) {
            wrappedText = if (softWrap) TextNode.wrapText(
                text,
                font,
                constraints.maxWidth.toFloat(),
                props.enforceSameWidthDigits
            )
            else text
            textMetrics = font.textDimensions(wrappedText)
            props.text = wrappedText
        }
        val constrained = constraints.constrain(
            Constraints(
                minWidth = textMetrics.width.toInt(),
                minHeight = textMetrics.height.toInt()
            )
        )
        return layout(constrained.minWidth, constrained.minHeight) {
        }
    }
}

@Composable
fun Text(
    text: String,
    modifier: Modifier = Modifier,
    font: Font = LocalSizes.current.normalText,
    fontSize: Float? = null,
    color: Color? = null,
    softWrap: Boolean = true,
    style: TextStyle = LocalTextStyle.current,
) {
    //TODO UiSurface.applyFontScale can change the scale of text, won't be reflected back in compose so we always copy it
    val font = remember(font, fontSize) {
        font.derive(fontSize ?: font.sizePts)
    }
    val textColor = color ?: style.color ?: LocalContentColor.current
    val measurePolicy = remember(text, font, softWrap) { TextMeasurePolicy(text, font, softWrap) }
    Layout(
        measurePolicy,
        modifier.drawBehind {
            //TODO remove debug rect
//            surface.getMeshLayer(0).uiPrimitives.rect(x, y, width, height, clipBounds, Color.GREEN)
            surface.getMeshLayer(0).getTextBuilder(font).withColor(textColor) {
                (this as MeshBuilder<UiTextVertexLayout>).vertexCustomizer = setBoundsTextVertexNoClip
                withTransform {
                    translate(x, y, 0f)
                    translate(0f, measurePolicy.textMetrics.yBaseline, 0f)
                    measurePolicy.props.origin.set(Vec3f.ZERO)
                    text(measurePolicy.props)
                }
            }
        }//.background(RectBackground(Color.RED))
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
