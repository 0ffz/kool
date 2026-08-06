package de.fabmax.kool.modules.compose.composables

import de.fabmax.kool.modules.compose.node.ComposeUiSurface

class DrawScopeImpl(
    override val x: Float,
    override val y: Float,
    override val width: Float,
    override val height: Float,
    override val surface: ComposeUiSurface,
    val content: () -> Unit,
) : ContentDrawScope {
    override fun drawContent() {
        content()
    }

    override val density: Float get() = 1f
    override val fontScale: Float get() = 1f

}