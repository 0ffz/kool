package de.fabmax.kool.modules.compose.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.currentComposer
import de.fabmax.kool.modules.compose.InternalKoolComposeAPI
import de.fabmax.kool.modules.compose.LocalUiSurface
import de.fabmax.kool.modules.compose.LocalZLayer
import de.fabmax.kool.modules.compose.UiNodeApplier
import de.fabmax.kool.modules.compose.composables.LayoutFunctions.SetMeasurePolicy
import de.fabmax.kool.modules.compose.composables.LayoutFunctions.SetModifier
import me.dvyy.compose.mini.layout.jetpack.MeasurePolicy
import me.dvyy.compose.mini.modifier.Modifier
import me.dvyy.compose.mini.modifier.materialize
import kotlin.jvm.JvmField

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
            set(measurePolicy, SetMeasurePolicy)
            set(materializedModifier, SetModifier)
        },
        content = content,
    )
}

// Constant values to avoid allocations of new lambda each call
object LayoutFunctions {
    @JvmField
    val SetModifier: ComposeUiNode.(Modifier) -> Unit = { setModifier(it) }

    @JvmField
    val SetMeasurePolicy: ComposeUiNode.(MeasurePolicy) -> Unit = { measurePolicy = it }

}