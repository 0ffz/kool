package de.fabmax.kool.modules.compose.composables

import androidx.compose.ui.unit.Constraints
import de.fabmax.kool.input.KeyEvent
import de.fabmax.kool.modules.compose.modifiers.ClickModifierNode
import de.fabmax.kool.modules.compose.modifiers.KeyInputModifierNode
import de.fabmax.kool.modules.compose.node.ComposeUiSurface
import de.fabmax.kool.modules.ui2.PointerEvent
import me.dvyy.compose.mini.layout.jetpack.*
import me.dvyy.compose.mini.modifier.Modifier
import me.dvyy.compose.mini.modifier.ModifierWrapper

class PositionedNode(
    override var node: Modifier.Node,
    val root: ComposeUiNode,
) : ModifierWrapper<PositionedNode>, Measurable, Placeable.PlacementScope, MeasureScope {
    override var next: PositionedNode? = null
    private var measureResult: MeasureResult = NotMeasured
    override var x: Int = 0
    override var y: Int = 0
    val width get() = placeable.width
    val height get() = placeable.height

    override val parentData: Any?
        get() {
            val node = node
            return if (node is ParentDataModifierNode) {
                node.run { modifyParentData(next?.parentData) }
            } else next?.parentData
        }

    private val placeable = object : Placeable() {
        override var width: Int = 0
        override var height: Int = 0

        override fun placeAt(x: Int, y: Int) {
            this@PositionedNode.x = x
            this@PositionedNode.y = y
            measureResult.placeChildren()
        }
    }

    // By default delegate to next node
    override fun measure(constraints: Constraints): Placeable {
        val node = node
        val result = when {
            next == null -> root.measurePolicy.run {
                this@PositionedNode.measure(
                    root.children.map { it.layoutDelegate },
                    constraints
                )
            }

            node is LayoutModifierNode -> {
                node.run { this@PositionedNode.measure(next!!, constraints) }
            }

            else -> {
                val placeable = next!!.measure(constraints)
                object : MeasureResult {
                    override val width: Int = placeable.width
                    override val height: Int = placeable.height
                    override fun placeChildren() {
                        placeable.place(0, 0)
                    }
                }
            }
        }
        measureResult = result
        placeable.width = result.width
        placeable.height = result.height
        return placeable
    }

    fun placeAt(x: Int, y: Int) {
        this.x = x
        this.y = y
        measureResult.placeChildren()
    }

    fun drawTo(surface: ComposeUiSurface) {
        if (next == null) return root.children.forEach { it.layoutDelegate.drawTo(surface) }
        val node = node
        if (node is DrawModifierNode) {
            val scope = DrawScopeImpl(
                x.toFloat(),
                y.toFloat(),
                width.toFloat(),
                height.toFloat(),
                surface,
                content = { next!!.drawTo(surface) })
            node.run { scope.draw() }
        } else next!!.drawTo(surface)
    }

    fun isInBounds(event: PointerEvent) =
        (event.screenPosition.x.toInt() in x..(x + width) && event.screenPosition.y.toInt() in y..(y + height))

    fun processPointerEvent(event: PointerEvent): Boolean {
        if (!isInBounds(event)) return false
        if (next == null) return root.children.any { it.layoutDelegate.processPointerEvent(event) }
        event.position.set(event.screenPosition.x - x, event.screenPosition.y - y)
        val node = node
        if (node !is ClickModifierNode) return next!!.processPointerEvent(event)
        node.onEvent(event)
        val consumedClick = event.isConsumed
        if (!consumedClick) return next!!.processPointerEvent(event)
        return consumedClick
    }

    fun processKeyEvent(event: KeyEvent): Boolean {
        if (next == null) return root.children.any { it.layoutDelegate.processKeyEvent(event) }
        val node = node
        if (node is KeyInputModifierNode && node.onKeyEvent(event))
            return true
        return next!!.processKeyEvent(event)
    }
}