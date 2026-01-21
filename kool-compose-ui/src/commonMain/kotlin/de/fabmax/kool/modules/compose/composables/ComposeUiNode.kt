package de.fabmax.kool.modules.compose.composables

import androidx.compose.runtime.Stable
import de.fabmax.kool.math.Vec4f
import de.fabmax.kool.modules.compose.modifiers.edit
import de.fabmax.kool.modules.compose.node.ComposeUiSurface
import de.fabmax.kool.modules.ui2.UiModifier
import de.fabmax.kool.modules.ui2.UiNode
import me.dvyy.compose.mini.layout.ChildMeasurePolicy
import me.dvyy.compose.mini.layout.Measurable
import me.dvyy.compose.mini.layout.MeasurePolicy
import me.dvyy.compose.mini.layout.Placeable
import me.dvyy.compose.mini.layout.jetpack.Constraints
import me.dvyy.compose.mini.layout.jetpack.IntOffset
import me.dvyy.compose.mini.layout.jetpack.IntSize
import me.dvyy.compose.mini.modifier.Modifier

data class SizeModifier(
    val constraints: Constraints,
) : Modifier.Element, LayoutChangingModifier {
    //TODO cleanup
    fun mergeWith(other: SizeModifier) = with(constraints) {
        SizeModifier(
            Constraints(
                other.constraints.minWidth.coerceIn(minWidth, maxWidth),
                other.constraints.maxWidth.coerceIn(minWidth, maxWidth),
                other.constraints.minHeight.coerceIn(minHeight, maxHeight),
                other.constraints.maxHeight.coerceIn(minHeight, maxHeight),
            )
        )
    }

    override fun modifyInnerConstraints(constraints: Constraints): Constraints {
        return SizeModifier(constraints).mergeWith(this).constraints
    }
}

/**
 * Sets min and max, width and height constraints for this element.
 */
@Stable
fun Modifier.sizeIn(
    minWidth: Int = 0,
    maxWidth: Int = Int.MAX_VALUE,
    minHeight: Int = 0,
    maxHeight: Int = Int.MAX_VALUE,
) = edit<ComposeUiModifier> {
    it.onLayout += SizeModifier(Constraints(minWidth, maxWidth, minHeight, maxHeight))
}

/** Sets identical min/max width and height constraints for this element. */
@Stable
fun Modifier.size(width: Int, height: Int) = sizeIn(width, width, height, height)

/** Sets identical min/max width and height constraints for this element. */
@Stable
fun Modifier.size(size: Int) = size(size, size)

/** Sets identical min/max width constraints for this element. */
@Stable
fun Modifier.width(width: Int) = sizeIn(width, width, 0, Int.MAX_VALUE)

/** Sets identical min/max height constraints for this element. */
@Stable
fun Modifier.height(height: Int) = sizeIn(0, Int.MAX_VALUE, height, height)

class ComposeUiModifier() : UiModifier(null) {
    var onLayout: MutableList<LayoutChangingModifier> by listProperty()
    val onRender: MutableList<ComposeUiSurface.(node: ComposeUiNode) -> Unit> by listProperty()
}

/**
 * An instance of [UiNode] with no additional state defined.
 *
 * Used by some layout composables to define behaviour via Modifiers instead of inheritance.
 */
class ComposeUiNode(val surface: ComposeUiSurface) : Placeable, Measurable {
    var parent: ComposeUiNode? = null
    val children = mutableListOf<ComposeUiNode>()
    val modifier: ComposeUiModifier = ComposeUiModifier()

    var measurePolicy: MeasurePolicy = ChildMeasurePolicy

    override var width: Int = 0
    override var height: Int = 0
    var x: Int = 0
    var y: Int = 0


    val clipBounds
        get() = Vec4f(
            x.toFloat(),
            y.toFloat(),
            x.toFloat() + width.toFloat(),
            y.toFloat() + height.toFloat()
        )

    override fun placeAt(x: Int, y: Int) {
        val offset = modifier.onLayout.fold(IntOffset(x, y)) { acc, modifier ->
            modifier.modifyPosition(acc)
        }
        this.x = offset.x
        this.y = offset.y
    }

    fun render() {
        modifier.onRender.forEach { draw ->
            draw(surface, this)
        }
        children.forEach { it.render() }
    }

    override fun measure(constraints: Constraints): Placeable {
        val innerConstraints = modifier.onLayout.fold(constraints) { inner, modifier ->
            modifier.modifyInnerConstraints(inner)
        }
        val result = measurePolicy.measure(children as List<ComposeUiNode>, innerConstraints)
        width = result.width
        height = result.height
        result.placer.placeChildren()

        val layoutConstraints = modifier.onLayout.fold(constraints) { outer, modifier ->
            modifier.modifyLayoutConstraints(IntSize(result.width, result.height), outer)
        }
        // Returned constraints will always appear as though they are in parent's bounds
        return coercedConstraints(layoutConstraints)
    }

    private fun coercedConstraints(constraints: Constraints) = with(constraints) {
        object : Placeable by this@ComposeUiNode {
            override var width: Int = this@ComposeUiNode.width.coerceIn(minWidth..maxWidth)
            override var height: Int = this@ComposeUiNode.height.coerceIn(minHeight..maxHeight)
        }
    }
}

interface LayoutChangingModifier {
    fun modifyPosition(offset: IntOffset): IntOffset = offset

    /** Modify constraints as they appear to parent nodes laying out this node. */
    fun modifyLayoutConstraints(measuredSize: IntSize, constraints: Constraints): Constraints =
        modifyInnerConstraints(constraints)

    /** Modify constraints as they appear to this node and its children for layout. */
    fun modifyInnerConstraints(constraints: Constraints): Constraints = constraints
}
