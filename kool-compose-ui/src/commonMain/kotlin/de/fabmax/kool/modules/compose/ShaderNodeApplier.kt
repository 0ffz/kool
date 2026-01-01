package de.fabmax.kool.modules.compose

import androidx.compose.runtime.AbstractApplier

class ShaderNodeApplier(root: ExampleShaderNode) : AbstractApplier<ExampleShaderNode>(root) {
    override fun insertBottomUp(index: Int, instance: ExampleShaderNode) {
        current.children.add(index, instance)
        check(instance.parent == null) {
            "$instance must not have a parent when being inserted."
        }
        instance.parent = current
    }

    // Example uses bottom-up tree building
    override fun insertTopDown(index: Int, instance: ExampleShaderNode) = Unit

    // Only needed for recomposition
    override fun remove(index: Int, count: Int) = Unit

    override fun move(from: Int, to: Int, count: Int) = Unit

    override fun onClear() = Unit
}
