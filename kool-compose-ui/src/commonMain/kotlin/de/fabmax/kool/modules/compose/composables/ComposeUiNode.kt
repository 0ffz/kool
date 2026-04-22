package de.fabmax.kool.modules.compose.composables

import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import de.fabmax.kool.math.Vec4f
import de.fabmax.kool.modules.compose.modifiers.ClickModifierNode
import de.fabmax.kool.modules.compose.node.ComposeUiSurface
import de.fabmax.kool.modules.ui2.PointerEvent
import de.fabmax.kool.modules.ui2.UiNode
import me.dvyy.compose.mini.layout.ComposeMiniNode
import me.dvyy.compose.mini.layout.jetpack.*
import me.dvyy.compose.mini.layout.jetpack.Placeable.PlacementScope
import me.dvyy.compose.mini.layout.jetpack.modifier.LayoutModifierNode
import me.dvyy.compose.mini.modifier.DelegatableNode
import me.dvyy.compose.mini.modifier.Modifier
import me.dvyy.compose.mini.modifier.ModifierWrapper
import me.dvyy.compose.mini.modifier.NodeChain

/**
 * An instance of [UiNode] with no additional state defined.
 *
 * Used by some layout composables to define behaviour via Modifiers instead of inheritance.
 */
class ComposeUiNode(val surface: ComposeUiSurface) : ComposeMiniNode() {
    var parent: ComposeUiNode? = null
    val children = mutableListOf<ComposeUiNode>()
    override val density: Float get() = 1f
    override val fontScale: Float get() = 1f
    val nodeChain = NodeChain { PositionedNode(it, this) }

    override var parentData: Any? = null

    override fun setModifier(modifier: Modifier) {
        nodeChain.updateFrom(modifier)
        parentData = nodeChain.topNode.parentData
    }

    override fun measure(constraints: Constraints): Placeable {
        val result = nodeChain.topNode.measure(constraints)
        width = result.width
        height = result.height
        return this
    }

    override fun placeAt(x: Int, y: Int) {
        this.x = x
        this.y = y
        nodeChain.topNode.place(x, y)
    }

    fun measureAndPlace(constraints: Constraints) {
        val placeable = measure(constraints)
        placeable.place(0, 0)
    }

    fun render() {
        nodeChain.topNode.drawTo(surface)

    }

    fun processClick(event: PointerEvent): Boolean {
        return nodeChain.topNode.doClick(event) || children.any { it.processClick(event) }
    }
}


interface DrawModifierNode : DelegatableNode {
    fun DrawScope.draw()
}

class PositionedNode(
    var wrapped: Modifier.Node,
    val root: ComposeUiNode,
) : ModifierWrapper<PositionedNode>, Measurable, PlacementScope, MeasureScope {
    override var next: PositionedNode? = null
    private var measureResult: MeasureResult = NotMeasured
    override var x: Int = 0
    override var y: Int = 0
    val width get() = placeable.width
    val height get() = placeable.height

    override val parentData: Any?
        get() {
            val node = wrapped
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
        val node = wrapped
        val result = when {
            next == null -> root.measurePolicy.run {
                this@PositionedNode.measure(
                    root.children as List<ComposeUiNode>,
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

    fun place(x: Int, y: Int) {
        this.x = x
        this.y = y
        measureResult.placeChildren()
    }

    fun drawTo(surface: ComposeUiSurface) {
        if (next == null) return root.children.forEach { it.render() }
        val node = wrapped
        if (node is DrawModifierNode) {
            val scope = DrawScope(
                x.toFloat(),
                y.toFloat(),
                width.toFloat(),
                height.toFloat(),
                surface,
                drawContent = { next!!.drawTo(surface) })
            node.run { scope.draw() }
        } else next!!.drawTo(surface)
    }

    fun isInBounds(event: PointerEvent) =
        (event.screenPosition.x.toInt() in x..(x + width) && event.screenPosition.y.toInt() in y..(y + height))

    fun doClick(event: PointerEvent): Boolean {
        if (next == null) return root.children.any { it.processClick(event) }
        if (!isInBounds(event)) return false
        event.position.set(event.screenPosition.x - x, event.screenPosition.y - y)
        val node = wrapped
        if (node !is ClickModifierNode) return next!!.doClick(event)
        val consumedClick = node.onClick(event)
        if (!consumedClick) return next!!.doClick(event)
        return consumedClick
    }
}

class DrawScope(
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val surface: ComposeUiSurface,
    val drawContent: () -> Unit,
) : Density {
    override val density: Float get() = 1f
    override val fontScale: Float get() = 1f

    val clipBounds get() = Vec4f(x, y, x + width, y + height)
}