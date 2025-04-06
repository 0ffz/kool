package de.fabmax.kool.modules.compose

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.Snapshot
import de.fabmax.kool.KoolContext
import de.fabmax.kool.modules.ui2.UiSurface
import de.fabmax.kool.util.RenderLoop
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class UiSurfaceComposition(
    val surface: UiSurface,
    val ctx: KoolContext,
) {
    var hasFrameWaiters = false
    val clock = BroadcastFrameClock { hasFrameWaiters = true }
    val composeScope = (CoroutineScope(Dispatchers.RenderLoop) + clock).also { scope ->
        surface.onRelease { scope.cancel() }
    }
    val coroutineContext: CoroutineContext = composeScope.coroutineContext

    private val rootNode = surface.viewport

    var running = false
    private val recomposer = Recomposer(coroutineContext)
    private val composition = Composition(UiNodeApplier(rootNode), recomposer)

    var applyScheduled = false
    val snapshotHandle = Snapshot.registerGlobalWriteObserver {
        if (!applyScheduled) {
            applyScheduled = true
            composeScope.launch {
                applyScheduled = false
                Snapshot.sendApplyNotifications()
            }
        }
    }

    var exitScheduled = false

    fun exit() {
        exitScheduled = true
    }

    fun start(content: @Composable () -> Unit) {
        !running || return
        running = true

//        GuiyScopeManager.scopes += composeScope
        composeScope.launch {
            recomposer.runRecomposeAndApplyChanges()
        }

        composeScope.launch {
            setContent(content)
            while (!exitScheduled) {
                if (hasFrameWaiters) {
                    hasFrameWaiters = false
                    surface.triggerUpdate()
                }
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
                LocalSizes provides surface.sizes
            ) {
                content()
            }
        }
    }
}
