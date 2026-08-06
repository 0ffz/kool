package de.fabmax.kool.modules.compose.composables

import me.dvyy.compose.mini.modifier.DelegatableNode

interface DrawModifierNode : DelegatableNode {
    fun ContentDrawScope.draw()
}