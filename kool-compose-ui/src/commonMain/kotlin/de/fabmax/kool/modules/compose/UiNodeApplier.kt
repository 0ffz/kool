package de.fabmax.kool.modules.compose

import androidx.compose.runtime.AbstractApplier
import de.fabmax.kool.modules.compose.composables.ComposeUiNode
import de.fabmax.kool.modules.ui2.UiNode

/**
 * Class to let compose manage [UiNode]'s children.
 */
class UiNodeApplier(
    root: ComposeUiNode,
    private val onChanges: () -> Unit = {},
) : AbstractApplier<ComposeUiNode>(root) {

    override fun onBeginChanges() {
        super.onBeginChanges()

        // We invoke this here rather than in the end change callback to try and ensure
        // no one relies on it to signal the end of changes.
        onChanges.invoke()
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
