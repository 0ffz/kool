package de.fabmax.kool.modules.compose.surface

import androidx.compose.runtime.Composable
import de.fabmax.kool.modules.ui2.Colors
import de.fabmax.kool.modules.ui2.Sizes
import de.fabmax.kool.modules.ui2.UiSurface

fun ComposableSurface(
    colors: Colors = Colors.darkColors(),
    sizes: Sizes = Sizes.medium,
    content: @Composable () -> Unit,
): UiSurface {
    val surface = UiSurface(colors, sizes, clearOnUpdateUi = false)
    val composition = UiSurfaceComposition(surface)
    composition.start {
        content()
    }
    surface.onRelease { composition.exit() }
    return surface
}
