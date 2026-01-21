package de.fabmax.kool.modules.compose.node

import de.fabmax.kool.math.Vec3f
import de.fabmax.kool.modules.compose.composables.ComposeUiNode
import de.fabmax.kool.modules.ui2.UiSurface.MeshLayer
import de.fabmax.kool.scene.Node
import de.fabmax.kool.scene.Scene
import de.fabmax.kool.util.SortedMap
import me.dvyy.compose.mini.layout.jetpack.Constraints

class ComposeUiSurface(
    scene: Scene,
    name: String = "uiSurface",
) : Node(name) {
    private val meshLayers = SortedMap<Int, MeshLayer>()
    val windows = mutableListOf<ComposeUiNode>()
    var maxWidth = 0f
    var maxHeight = 0f
    val scope = scene.coroutineScope
    private var requiresUpdate = true

    fun triggerUpdate() {
        requiresUpdate = true
    }

    fun addWindow(window: ComposeUiNode): ComposeUiNode {
        windows.add(window)
        return window
    }

    fun removeWindow(window: ComposeUiNode) {
        windows.remove(window)
    }

    private val mirrorTransformScale = Vec3f(1f, -1f, 1f)

    init {
        transform.scale(mirrorTransformScale)
        onUpdate {
            maxWidth = it.viewport.width.toFloat()
            maxHeight = it.viewport.height.toFloat()
            if (isVisible) {
                if (requiresUpdate) {
                    requiresUpdate = false
                    update()
                }
            }
        }
    }

    fun getMeshLayer(layer: Int): MeshLayer {
        val meshLayer = meshLayers[layer] ?: MeshLayer("$name/MeshLayer[$layer]").also { meshLayers[layer] = it }
        meshLayer.isUsed = true
        return meshLayer
    }

    fun update() {
        meshLayers.values.forEach {
            it.clear()
            removeNode(it)
        }
        val placeables = windows.map {
            it.measure(Constraints(maxWidth = maxWidth.toInt(), maxHeight = maxHeight.toInt()))
        }
        placeables.forEach { it.placeAt(0, 0) }
        windows.forEach {
            it.render()
        }
        // re-add mesh layers in correct order
        meshLayers.values.forEach {
            if (it.isUsed) {
                addNode(it)
            }
        }
    }
}