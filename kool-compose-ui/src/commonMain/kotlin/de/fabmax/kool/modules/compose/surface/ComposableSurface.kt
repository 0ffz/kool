package de.fabmax.kool.modules.compose.surface

import androidx.compose.runtime.Composable
import de.fabmax.kool.modules.ui2.Colors
import de.fabmax.kool.modules.ui2.Sizes
import de.fabmax.kool.modules.ui2.UiSurface
import de.fabmax.kool.scene.Scene

fun ComposableSurface(
    scene: Scene,
    colors: Colors = Colors.darkColors(),
    sizes: Sizes = Sizes.medium,
    content: @Composable () -> Unit,
): UiSurface {
    val surface = UiSurface(scene, colors, sizes, clearOnUpdateUi = false).apply {
        inputMode = UiSurface.InputCaptureMode.CaptureOverBackground
    }
    val composition = UiSurfaceComposition(surface)
    composition.start {
        content()
    }
    surface.onRelease { composition.exit() }
    return surface
}
