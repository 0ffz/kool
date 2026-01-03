package de.fabmax.kool.modules.compose.surface

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.Snapshot
import de.fabmax.kool.modules.compose.LocalColors
import de.fabmax.kool.modules.compose.LocalSizes
import de.fabmax.kool.modules.compose.LocalUiSurface
import de.fabmax.kool.modules.compose.UiNodeApplier
import de.fabmax.kool.modules.ui2.*
import de.fabmax.kool.util.KoolDispatchers
import kotlinx.coroutines.*
import me.dvyy.compose.minimal.me.dvyy.compose.minimal.runtime.MinimalComposition

/**
 * Manages a composition for a given [UiSurface].
 *
 * The composition will automatically add and remove [UiNode]s to the surface's viewport,
 * modelling layers as [BoxNode]s directly under the surface's viewport.
 *
 * The surface is responsible for calling [exit] when released to stop further recompositions, effects, etc...
 */
class UiSurfaceComposition(
    val surface: UiSurface,
) {
    private val viewport: UiNode = surface.viewport

    val composition = MinimalComposition<UiNode>(
        onFrameAwaiters = {
            surface.triggerUpdate()
        },
        coroutineContext = surface.parentScene.coroutineScope.coroutineContext,
        wrapContent = { content ->
            CompositionLocalProvider(
                LocalUiSurface provides surface,
                LocalColors provides surface.colors,
                LocalSizes provides surface.sizes,
            ) {
                content()
            }

        },
        createLayerNode = {
            viewport.Box {
                this.modifier.resetDefaults()
                modifier.zLayer(0).size(Grow.Std, Grow.Std)
            } as BoxNode
        },
        removeLayerNode = { viewport.mutChildren.remove(it) },
        applierForNode = { UiNodeApplier(it) },
    )
}
