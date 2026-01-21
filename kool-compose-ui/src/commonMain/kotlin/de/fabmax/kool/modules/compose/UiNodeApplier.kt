package de.fabmax.kool.modules.compose

import androidx.compose.runtime.AbstractApplier
import de.fabmax.kool.modules.compose.composables.ComposeUiNode
import de.fabmax.kool.modules.ui2.UiNode

/**
 * Class to let compose manage [UiNode]'s children.
 */
class UiNodeApplier(root: ComposeUiNode) : AbstractApplier<ComposeUiNode>(root) {
    var changed = false

    override fun onBeginChanges() {
        super.onBeginChanges()
        changed = true
    }

    override fun insertTopDown(index: Int, instance: ComposeUiNode) {
        // Ignored, we insert bottom-up.
    }

    override fun insertBottomUp(index: Int, instance: ComposeUiNode) {
        current.children.add(index, instance)
        check(instance.parent == null) {
            "$instance must not have a parent when being inserted."
        }
        instance.parent = current
    }

    override fun remove(index: Int, count: Int) {
        current.children.remove(index, count)
    }

    override fun move(from: Int, to: Int, count: Int) {
        current.children.move(from, to, count)
    }

    override fun onClear() {
        current.children.clear()
    }
}
