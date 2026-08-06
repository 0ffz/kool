package de.fabmax.kool.modules.compose.node

import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.unit.Constraints
import de.fabmax.kool.KoolContext
import de.fabmax.kool.input.InputStack
import de.fabmax.kool.input.KeyEvent
import de.fabmax.kool.input.PointerState
import de.fabmax.kool.math.Vec3f
import de.fabmax.kool.modules.compose.composables.ComposeUiNode
import de.fabmax.kool.modules.compose.composables.toolkit.Focusable
import de.fabmax.kool.modules.ui2.PointerEvent
import de.fabmax.kool.modules.ui2.UiSurface.MeshLayer
import de.fabmax.kool.scene.Node
import de.fabmax.kool.scene.Scene
import de.fabmax.kool.util.SortedMap
import me.dvyy.compose.mini.layout.jetpack.MeasureScope
import me.dvyy.compose.mini.layout.jetpack.Placeable

class ComposeUiSurface(
    scene: Scene,
    name: String = "uiSurface",
) : Node(name) {
    private val meshLayers = SortedMap<Int, MeshLayer>()
    val windows = mutableListOf<ComposeUiNode>()
    var maxWidth = 0
    var maxHeight = 0
    val scope = scene.coroutineScope
    private var needsLayout = true
    private var needsDraw = true
    var focused: Focusable? = null
    var lastHovered: ComposeUiNode? = null

    fun needsLayout() {
        needsLayout = true
    }

    fun needsRedraw() {
        needsDraw = true
    }

    fun addWindow(window: ComposeUiNode): ComposeUiNode {
        windows.add(window)
        return window
    }

    fun removeWindow(window: ComposeUiNode) {
        windows.remove(window)
    }

    private val mirrorTransformScale = Vec3f(1f, -1f, 1f)


    private val readStatesOnLayout = mutableSetOf<Any>()//TODO mutableScatterSetOf<Any>()
    private val readStatesOnLayoutObserver: (Any) -> Unit = readStatesOnLayout::add

    private val readStatesOnDraw = mutableSetOf<Any>()//TODO mutableScatterSetOf<Any>()
    private val readStatesOnDrawObserver: (Any) -> Unit = readStatesOnDraw::add

    init {
        transform.scale(mirrorTransformScale)
        val pointerListener = object : InputStack.InputHandler("ComposeUiSurface") {
            override fun handlePointer(pointerState: PointerState, ctx: KoolContext) {
                val pointer = pointerState.primaryPointer
                val pointerEvent = PointerEvent(pointer, ctx)
                pointerEvent.isConsumed = false
                windows.any { it.layoutDelegate.processPointerEvent(pointerEvent) }
            }

            override fun handleKeyEvents(keyEvents: List<KeyEvent>, ctx: KoolContext) {
                keyEvents.forEach { event ->
                    windows.any { it.layoutDelegate.processKeyEvent(event) }
                }
            }
        }
        InputStack.pushTop(pointerListener)

        // When state changes are written, request redraw/relayout if those states would affect the relevant phase
        val applyObserver = Snapshot.registerApplyObserver { changedStates, snapshot ->
            for (state in changedStates) {
                if (state in readStatesOnLayout) {
                    needsLayout()
                    needsRedraw()
                    // Already need everything updated, don't need to iterate futher
                    return@registerApplyObserver
                }
                if (!needsDraw && state in readStatesOnDraw) needsRedraw()
            }
        }

        onUpdate {
            val newWidth = it.viewport.width
            val newHeight = it.viewport.height
            if (newWidth != maxWidth || newHeight != maxHeight) needsLayout()
            maxWidth = newWidth
            maxHeight = newHeight
            if (isVisible) {
                update()
            }
        }

        onRelease {
            InputStack.remove(pointerListener)
            applyObserver.dispose()
        }
    }

    fun getMeshLayer(layer: Int): MeshLayer {
        val meshLayer = meshLayers[layer] ?: MeshLayer("$name/MeshLayer[$layer]").also { meshLayers[layer] = it }
        meshLayer.isUsed = true
        return meshLayer
    }
    private val rootLayoutScope = object : MeasureScope, Placeable.PlacementScope {
        override val x: Int = 0
        override val y: Int = 0
        override val density: Float get() = 1f
        override val fontScale: Float get() = 1f
    }

    fun update() {
        if (needsLayout) {
            needsLayout = false
            needsDraw = true

            // Measure and place all ui nodes, keeping track of states read during layout phase
            readStatesOnLayout.clear()
            Snapshot.observe(readObserver = readStatesOnLayoutObserver) {
                with(rootLayoutScope) {
                    windows.forEach {
                        it.layoutDelegate.measure(constraints = Constraints(maxWidth = maxWidth, maxHeight = maxHeight))
                        it.layoutDelegate.placeAt(0, 0)
                    }
                }
            }
        }
        if (needsDraw) {
            needsDraw = false
            meshLayers.values.forEach { layer ->
                layer.clear()
                removeNode(layer)
            }

            // Draw ui nodes to meshes, keeping track of states read during draws
            readStatesOnDraw.clear()
            Snapshot.observe(readObserver = readStatesOnDrawObserver) {
                windows.forEach {
                    it.layoutDelegate.drawTo(this@ComposeUiSurface)
                }
            }

            // re-add mesh layers in correct order
            meshLayers.values.forEach {
                if (it.isUsed) {
                    addNode(it)
                }
            }
        }
    }
}