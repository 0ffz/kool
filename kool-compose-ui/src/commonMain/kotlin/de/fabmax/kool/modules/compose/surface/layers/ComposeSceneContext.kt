package de.fabmax.kool.modules.compose.surface.layers

import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.compositionLocalOf
import de.fabmax.kool.modules.ui2.UiNode
import de.fabmax.kool.util.Viewport

/**
 * Context for the current compose scene (bound to a [de.fabmax.kool.modules.compose.surface.ComposableSurface]).
 *
 * Manages multiple layers in composition for one surface.
 * Loosely based on the androidx class of the same name.
 */
class ComposeSceneContext(
    val createSubNode: () -> UiNode,
    val removeNode: (UiNode) -> Unit,
) {
    private val layers = mutableListOf<ComposeSceneLayer>()

    /**
     * Creates a new layer for composition, this layer is resposible for calling
     * close when it is no longer needed.
     *
     * @param parentContext parent composition context, used for passing composition locals to this layer's subcomposition.
     */
    fun createLayer(
        parentContext: CompositionContext,
    ): ComposeSceneLayer {
        return ComposeSceneLayer(
            owner = this,
            parentContext = parentContext,
            layerRootNode = createSubNode()
        ).also { layers.add(it) }
    }

    fun removeLayer(layer: ComposeSceneLayer) {
        layers.remove(layer)
        removeNode(layer.layerRootNode)
    }
}


val LocalComposeSceneContext = compositionLocalOf<ComposeSceneContext> { error("No CompositionLayers provided") }
