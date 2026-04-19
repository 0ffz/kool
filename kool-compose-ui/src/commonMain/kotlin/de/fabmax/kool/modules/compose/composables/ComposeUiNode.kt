package de.fabmax.kool.modules.compose.composables

import androidx.compose.runtime.Stable
import de.fabmax.kool.math.Vec4f
import de.fabmax.kool.modules.compose.modifiers.edit
import de.fabmax.kool.modules.compose.node.ComposeUiSurface
import de.fabmax.kool.modules.ui2.UiModifier
import de.fabmax.kool.modules.ui2.UiNode
import me.dvyy.compose.mini.layout.*
import me.dvyy.compose.mini.layout.Placeable.PlacementScope
import me.dvyy.compose.mini.layout.jetpack.Constraints
import me.dvyy.compose.mini.layout.jetpack.modifier.LayoutModifier
import me.dvyy.compose.mini.modifier.Modifier

//data class SizeModifier(
//    val constraints: Constraints,
//) : Modifier.Element, LayoutChangingModifier {
//    //TODO cleanup
//    fun mergeWith(other: SizeModifier) = with(constraints) {
//        SizeModifier(
//            Constraints(
//                other.constraints.minWidth.coerceIn(minWidth, maxWidth),
//                other.constraints.maxWidth.coerceIn(minWidth, maxWidth),
//                other.constraints.minHeight.coerceIn(minHeight, maxHeight),
//                other.constraints.maxHeight.coerceIn(minHeight, maxHeight),
//            )
//        )
//    }
//
//    override fun modifyInnerConstraints(constraints: Constraints): Constraints {
//        return SizeModifier(constraints).mergeWith(this).constraints
//    }
//}

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
//    it.onLayout += SizeModifier(Constraints(minWidth, maxWidth, minHeight, maxHeight))
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
    var onLayout: MutableList<LayoutModifier> by listProperty()
    val onRender: MutableList<ComposeUiSurface.(node: ComposeUiNode) -> Unit> by listProperty()
}

internal object NotMeasured : MeasureResult {
    override val width get() = 0
    override val height get() = 0
    override fun placeChildren() = throw UnsupportedOperationException("Not measured")
}

/**
 * An instance of [UiNode] with no additional state defined.
 *
 * Used by some layout composables to define behaviour via Modifiers instead of inheritance.
 */
class ComposeUiNode(val surface: ComposeUiSurface) : Placeable(), Measurable, MeasureScope, PlacementScope {
    var parent: ComposeUiNode? = null
    val children = mutableListOf<ComposeUiNode>()
    val modifier: ComposeUiModifier = ComposeUiModifier()

    var measurePolicy: MeasurePolicy = ChildMeasurePolicy
    private var measureResult: MeasureResult = NotMeasured

    override var width: Int = 0
    override var height: Int = 0
    override var x: Int = 0
    override var y: Int = 0


    val clipBounds
        get() = Vec4f(
            x.toFloat(),
            y.toFloat(),
            x.toFloat() + width.toFloat(),
            y.toFloat() + height.toFloat()
        )

    override fun placeAt(x: Int, y: Int) {
//        val offset = modifier.onLayout.fold(IntOffset(x, y)) { acc, modifier ->
//            modifier.modifyPosition(acc)
//        }
        this.x = x
        this.y = y
        measureResult.placeChildren()
    }

    fun measureAndPlace() {
        val placeable = measure(Constraints())
        placeable.place(0, 0)
    }

    fun render() {
        modifier.onRender.forEach { draw ->
            draw(surface, this)
        }
        children.forEach { it.render() }
    }

    override fun measure(constraints: Constraints): Placeable {
        measureResult = doMeasure(constraints)
        width = measureResult.width
        height = measureResult.height
        return this
    }

    fun doMeasure(constraints: Constraints): MeasureResult {
//        val innerConstraints = modifier.onLayout.fold(constraints) { inner, modifier ->
//            modifier.modifyInnerConstraints(inner)
//        }
        val result = with(measurePolicy) { measure(children as List<ComposeUiNode>, constraints) }
        return result
//        width = result.width
//        height = result.height
//        result.placer.placeChildren()

//        val layoutConstraints = modifier.onLayout.fold(constraints) { outer, modifier ->
//            modifier.modifyLayoutConstraints(IntSize(result.width, result.height), outer)
//        }
        // Returned constraints will always appear as though they are in parent's bounds
//        return coercedConstraints(layoutConstraints)
//        return object : MeasureResult {
//            override val width: Int get() = result.width
//            override val height: Int get() = result.height
//
//            override fun placeChildren() {
//                result.place(0, 0)
//            }
//        }
    }

//    private fun coercedConstraints(constraints: Constraints) = with(constraints) {
//        object : Placeable by this@ComposeUiNode {
//            override var width: Int = this@ComposeUiNode.width.coerceIn(minWidth..maxWidth)
//            override var height: Int = this@ComposeUiNode.height.coerceIn(minHeight..maxHeight)
//        }
//    }
}
