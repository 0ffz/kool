package de.fabmax.kool.modules.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import de.fabmax.kool.modules.compose.modifiers.Modifier
import de.fabmax.kool.modules.ui2.UiNode
import de.fabmax.kool.modules.ui2.UiSurface

/**
 * The main component for layout, it measures and positions zero or more children.
 */
@Composable
inline fun <T: UiNode> Layout(
    noinline constructor: (parent: UiNode?, surface: UiSurface) -> T,
    modifier: Modifier,
    content: @Composable () -> Unit = {}
) {
    val surface = LocalUiSurface.current

    ComposeNode<UiNode, UiNodeApplier>(
        factory = { constructor(null, surface).also { it.applyDefaults() } },
        update = {
            set(modifier) {
                this.modifier.resetDefaults()
                modifier.reduce(this.modifier)
                 //TODO update modifier system to be able to set this value directly
            }
        },
        content = content,
    )
}
