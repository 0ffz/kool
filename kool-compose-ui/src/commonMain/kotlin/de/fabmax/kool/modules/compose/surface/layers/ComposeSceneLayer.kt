package de.fabmax.kool.modules.compose.surface.layers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composition
import androidx.compose.runtime.CompositionContext
import de.fabmax.kool.modules.compose.UiNodeApplier
import de.fabmax.kool.modules.ui2.UiNode

/**
 * A layer in a [de.fabmax.kool.modules.compose.surface.ComposableSurface]'s composition, created as a subcomposition.
 *
 * [de.fabmax.kool.modules.compose.surface.UiSurfaceComposition] binds [layerRootNode] to a [de.fabmax.kool.modules.ui2.BoxNode]
 * right under the scene's viewport. This lets us treat this node as part of the same tree in compose
 * (ex. for composition locals, getting parent node information, etc...), while Kool sees it as a node
 * directly under the root node.
 */
class ComposeSceneLayer(
    val owner: ComposeSceneContext,
    val parentContext: CompositionContext,
    val layerRootNode: UiNode,
) {
    val composition = Composition(UiNodeApplier(layerRootNode), parentContext)

    fun setContent(content: @Composable () -> Unit) {
        composition.setContent(content)
    }

    fun close() {
        owner.removeLayer(this)
        composition.dispose()
    }
}
