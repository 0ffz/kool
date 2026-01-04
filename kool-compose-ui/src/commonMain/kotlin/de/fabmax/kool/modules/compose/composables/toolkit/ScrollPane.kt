package de.fabmax.kool.modules.compose.composables.toolkit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import de.fabmax.kool.modules.compose.LocalUiSurface
import de.fabmax.kool.modules.compose.composables.Layout
import de.fabmax.kool.modules.compose.modifiers.edit
import de.fabmax.kool.modules.compose.modifiers.onWheelX
import de.fabmax.kool.modules.compose.modifiers.onWheelY
import de.fabmax.kool.modules.ui2.*
import de.fabmax.kool.util.Color
import me.dvyy.compose.minimal.modifier.Modifier

@Composable
fun rememberScrollState() = remember { ScrollState() }

@Composable
fun ScrollArea(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    content: @Composable () -> Unit,
) {
    val scrollState by rememberUpdatedState(scrollState)
    ScrollPane(modifier.onWheelX {
//            if (isScrollableHorizontal) {
        scrollState.scrollDpX(it.pointer.scroll.x * -20f)
//            }
    }
        .onWheelY {
//            if (isScrollableVertical) {
            scrollState.scrollDpY(it.pointer.scroll.y * -50f)
//            }
        }, scrollState) {
        content()
    }
//    VerticalScrollbar(Modifier.fillMaxHeight().alignX(AlignmentX.End),state = scrollState)
}

@Composable
fun ScrollPane(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    content: @Composable () -> Unit,
) {
    val surface = LocalUiSurface.current
    val scrollState by rememberUpdatedState(scrollState)
    val node = remember(surface) { ScrollPaneNode(null, surface) }
    node.state = scrollState

    Layout({ _, _ -> node }, modifier) {
        content()
    }
}

@Composable
fun VerticalScrollbar(
    modifier: Modifier = Modifier,
    state: ScrollState,
    scrollbarColor: Color? = null,
) {
    Layout(
        ::ScrollbarNode, modifier
            .edit<ScrollbarModifier> { modifier ->
                modifier
                    .relativeBarPos(state.relativeBarPosY)
                    .relativeBarLen(state.relativeBarLenY)
                    .onChange {
                        state.scrollRelativeY(it)
                    }
                scrollbarColor?.let { modifier.colors(it) }
            }
    )
}
