package de.fabmax.kool.modules.compose.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.currentComposer
import de.fabmax.kool.modules.compose.InternalKoolComposeAPI
import de.fabmax.kool.modules.compose.LocalUiSurface
import de.fabmax.kool.modules.compose.LocalZLayer
import de.fabmax.kool.modules.compose.UiNodeApplier
import de.fabmax.kool.modules.compose.modifiers.UiModifierWrapper
import me.dvyy.compose.mini.layout.MeasurePolicy
import me.dvyy.compose.mini.modifier.Modifier
import me.dvyy.compose.mini.modifier.materialize

/**
 * The main component for layout, it measures and positions zero or more children.
 */
@Composable
@InternalKoolComposeAPI
inline fun Layout(
    measurePolicy: MeasurePolicy,
    modifier: Modifier,
    content: @Composable () -> Unit = {},
) {
    val surface = LocalUiSurface.current
    val zLayer = LocalZLayer.current

    val materializedModifier = currentComposer.materialize(modifier)
    ComposeNode<ComposeUiNode, UiNodeApplier>(
        factory = { ComposeUiNode(surface) },
        update = {
            set(measurePolicy) { this.measurePolicy = it }
            set(materializedModifier) {
                this.modifier.resetDefaults()
                this.modifier.zLayer = zLayer
                it.foldOut(this.modifier) { modifier, uiModifier ->
                    if (modifier is UiModifierWrapper) modifier.applyTo(this.modifier)
                    uiModifier
                }
            }
            set(zLayer) { this.modifier.zLayer = it }
        },
        content = content,
    )
}
