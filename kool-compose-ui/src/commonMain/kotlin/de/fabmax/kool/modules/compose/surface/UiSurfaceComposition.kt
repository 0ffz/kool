package de.fabmax.kool.modules.compose.surface

import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.Snapshot
import de.fabmax.kool.modules.compose.*
import de.fabmax.kool.modules.compose.composables.rendering.TextStyle
import de.fabmax.kool.modules.compose.surface.layers.ComposeSceneContext
import de.fabmax.kool.modules.compose.surface.layers.LocalComposeSceneContext
import de.fabmax.kool.modules.ui2.*
import de.fabmax.kool.util.MainUI
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

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
    private var hasFrameWaiters = false
    private var running = false
    private var applyScheduled = false
    private var exitScheduled = false

    private val clock = BroadcastFrameClock {
        hasFrameWaiters = true
    }
    private val composeScope = (CoroutineScope(Dispatchers.MainUI) + clock).also { scope ->
        surface.onRelease { scope.cancel() }
    }
    private val snapshotHandle = Snapshot.registerGlobalWriteObserver {
        if (!applyScheduled) {
            applyScheduled = true
            composeScope.launch {
                applyScheduled = false
                Snapshot.sendApplyNotifications()
            }
        }
    }
    private val viewport: UiNode = surface.viewport
    private val coroutineContext: CoroutineContext = composeScope.coroutineContext
    private val recomposer = Recomposer(coroutineContext)
    private val composition = Composition(UiNodeApplier(createSubNode(layer = 0)), recomposer)

    /** Manages creating multiple layers in composition as box nodes under surface viewport. */
    private val layers = ComposeSceneContext(
        createSubNode = { createSubNode(layer = UiSurface.LAYER_POPUP) },
        removeNode = { viewport.mutChildren.remove(it) }
    )

    fun exit() {
        exitScheduled = true
    }

    fun start(content: @Composable () -> Unit) {
        !running || return
        running = true

        composeScope.launch {
            recomposer.runRecomposeAndApplyChanges()
        }

        surface.onUpdate.stageAdd({
        })
        composeScope.launch {
            setContent(content)
            while (!exitScheduled && !surface.isReleased) {
                if (hasFrameWaiters) {
                    hasFrameWaiters = false

//                    println("[${Time.frameCount}] (UiSurfaceComposition) trigger update")
                    surface.triggerUpdate()
                }
//                println("[${Time.frameCount}] (UiSurfaceComposition) Send clock frame")
                clock.sendFrame(nanoTime())
                yield()
            }
            running = false
            recomposer.close()
            snapshotHandle.dispose()
            composition.dispose()
            composeScope.cancel()
        }
    }

    private fun setContent(content: @Composable () -> Unit) {
        hasFrameWaiters = true
        composition.setContent {
            CompositionLocalProvider(
                LocalUiSurface provides surface,
                LocalColors provides surface.colors,
                LocalSizes provides surface.sizes,
                LocalTextStyle provides TextStyle(),
                LocalContentColor provides surface.colors.onBackground,
                LocalComposeSceneContext provides layers,
            ) {
                content()
            }
        }
    }

    private fun createSubNode(layer: Int) = viewport.Box {
        modifier.zLayer(layer).size(Grow.Std, Grow.Std)
    } as BoxNode
}
