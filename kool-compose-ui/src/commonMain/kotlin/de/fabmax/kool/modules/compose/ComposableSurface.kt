package de.fabmax.kool.modules.compose

import androidx.compose.runtime.Composable
import de.fabmax.kool.KoolContext
import de.fabmax.kool.modules.ui2.Colors
import de.fabmax.kool.modules.ui2.Sizes
import de.fabmax.kool.modules.ui2.UiSurface
import de.fabmax.kool.scene.Node

fun ComposableSurface(
    ctx: KoolContext,
    colors: Colors = Colors.Companion.darkColors(),
    sizes: Sizes = Sizes.Companion.medium,
    content: @Composable () -> Unit
): Node {
    val surface = UiSurface(colors, sizes, clearOnUpdateUi = false)
    val owner = UiSurfaceComposition(surface, ctx)
    owner.start {
        content()
    }
    surface.onRelease { owner.exit() }
    return surface
}
