package de.fabmax.kool.modules.compose.composables

import de.fabmax.kool.modules.compose.node.ComposeUiSurface
import de.fabmax.kool.modules.ui2.UiNode
import me.dvyy.compose.mini.layout.ComposeMiniNode
import me.dvyy.compose.mini.modifier.Modifier
import me.dvyy.compose.mini.modifier.NodeChain
import me.dvyy.compose.mini.modifier.WrappingChain

/**
 * An instance of [UiNode] with no additional state defined.
 *
 * Used by some layout composables to define behaviour via Modifiers instead of inheritance.
 */
class ComposeUiNode : ComposeMiniNode() {
    lateinit var surface: ComposeUiSurface
    var parent: ComposeUiNode? = null
    val children = mutableListOf<ComposeUiNode>()
    val nodeChain = NodeChain()
    val wrappingChain = WrappingChain(nodeChain) {
        PositionedNode(
            it,
            this
        )
    }//nodeChain.wrappingNode(Nodes.Layout, { MeasureData(it) }) { MeasureChildren() }
    val layoutDelegate get() = wrappingChain.head
//    val drawDelegate = nodeChain.wrappingNode(Draw, { MeasureData(it) }) { MeasureChildren() }

    //    override var parentData: Any? = null
    override fun setModifier(modifier: Modifier) {
        nodeChain.updateFrom(modifier)
        wrappingChain.update()
    }
}


