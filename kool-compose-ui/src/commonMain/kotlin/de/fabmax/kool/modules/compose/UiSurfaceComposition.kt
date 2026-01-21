package de.fabmax.kool.modules.compose

import androidx.compose.runtime.BroadcastFrameClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import de.fabmax.kool.modules.compose.composables.ComposeUiNode
import de.fabmax.kool.modules.compose.composables.rendering.TextStyle
import de.fabmax.kool.modules.compose.node.ComposeUiSurface
import de.fabmax.kool.modules.ui2.*
import de.fabmax.kool.util.Time
import kotlinx.coroutines.launch
import me.dvyy.compose.mini.runtime.MinimalComposition
import kotlin.time.ExperimentalTime

/**
 * Manages a composition for a given [UiSurface].
 *
 * The composition will automatically add and remove [UiNode]s to the surface's viewport,
 * modelling layers as [BoxNode]s directly under the surface's viewport.
 *
 * The surface is responsible for calling [exit] when released to stop further recompositions, effects, etc...
 */
@OptIn(ExperimentalTime::class)
class UiSurfaceComposition(
    val surface: ComposeUiSurface,
    val sizes: Sizes,
    val colors: Colors,
) {
    //    private val viewport: UiNode = surface.viewport
    private val clock = BroadcastFrameClock()
    private val contentCompat = SurfaceContentCompat()

    init {
        surface.scope.launch {
            while (true) {
                Time.composeFrameClock.withFrameNanos {
                    clock.sendFrame(it)
                }
            }
        }
    }

    private val composition = MinimalComposition<ComposeUiNode>(
        onNodesChanged = {
            surface.triggerUpdate()
        },
        coroutineContext = surface.scope.coroutineContext + clock,
        wrapContent = { content ->
            CompositionLocalProvider(
                LocalUiSurface provides surface,
                LocalColors provides colors,
                LocalSizes provides sizes,
                LocalTextStyle provides TextStyle(),
                LocalContentColor provides colors.onBackground,
                LocalSurfaceContentCompat provides contentCompat,
            ) {
                content()
            }

        },
        createLayerNode = {
            surface.addWindow(ComposeUiNode(surface))
//            viewport.Box {
//                this.modifier.resetDefaults()
//                modifier.zLayer(UiSurface.LAYER_POPUP).size(Grow.Std, Grow.Std)
//            } as BoxNode
        },
        removeLayerNode = { surface.removeWindow(it) },
        applierForNode = { UiNodeApplier(it) },
    )

    fun start(content: @Composable () -> Unit) {
        composition.start { content() }
    }

    fun exit() {
        composition.close()
    }
}