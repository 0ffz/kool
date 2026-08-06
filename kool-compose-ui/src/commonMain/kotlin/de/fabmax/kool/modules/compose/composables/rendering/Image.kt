package de.fabmax.kool.modules.compose.composables.rendering

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.constrain
import de.fabmax.kool.modules.compose.composables.Layout
import de.fabmax.kool.modules.compose.modifiers.drawBehind
import de.fabmax.kool.modules.ui2.FlatImageProvider
import de.fabmax.kool.modules.ui2.ImageShader
import de.fabmax.kool.modules.ui2.ImageSize
import de.fabmax.kool.modules.ui2.UiVertexLayout
import de.fabmax.kool.pipeline.Texture2d
import de.fabmax.kool.scene.geometry.MeshBuilder
import de.fabmax.kool.util.Color
import de.fabmax.kool.util.set
import me.dvyy.compose.mini.layout.jetpack.Measurable
import me.dvyy.compose.mini.layout.jetpack.MeasurePolicy
import me.dvyy.compose.mini.layout.jetpack.MeasureResult
import me.dvyy.compose.mini.layout.jetpack.MeasureScope
import me.dvyy.compose.mini.modifier.Modifier

class ImageMeasurePolicy(
    val texture: Texture2d,
) : MeasurePolicy {
    override fun MeasureScope.measure(
        measurables: List<Measurable>,
        constraints: Constraints,
    ): MeasureResult {
        val constrainedSize = constraints.constrain(IntSize(texture.width, texture.height))
        return layout(constrainedSize.width, constrainedSize.height) {}
    }

}

//@PublishedApi
//internal val setBoundsUiVertex: MutableStructBufferView<UiVertexLayout>.(UiVertexLayout) -> Unit =

@Composable
fun Image(
    texture: Texture2d,
    tint: Color? = null,
    size: ImageSize? = null,
    imageZ: Int? = null,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val provider = remember(texture) { FlatImageProvider(texture) }
    val measurePolicy = remember(texture) { ImageMeasurePolicy(texture) }
    val shader = remember(texture) { ImageShader().apply { image = texture } }
    Layout(
        measurePolicy,
        modifier.drawBehind {
            val imgMesh = surface.getMeshLayer(1).addImage(texture)
            imgMesh.builder.clear()
            (imgMesh.builder as MeshBuilder<UiVertexLayout>).vertexCustomizer = {
                it.clip.set(x, y, x + width, y + height)
            }
            imgMesh.builder.withTransform {
                translate(x, y, 0f)
                rect {
//                    isCenteredOrigin = false
                    texCoordLowerLeft.set(provider.uvBottomLeft)
                    texCoordLowerRight.set(provider.uvBottomRight)
                    texCoordUpperLeft.set(provider.uvTopLeft)
                    texCoordUpperRight.set(provider.uvTopRight)
//                    this.origin.set(0f, 0f, 0f)
                    this.size.set(width, height)
                }
            }
            imgMesh.applyShader(texture, null)
        }
    ) {
        content()
    }
}
