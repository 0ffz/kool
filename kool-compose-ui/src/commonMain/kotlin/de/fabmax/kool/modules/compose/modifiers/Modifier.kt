package de.fabmax.kool.modules.compose.modifiers

import androidx.compose.runtime.Immutable
import de.fabmax.kool.modules.ui2.Dimension
import de.fabmax.kool.modules.ui2.Layout
import de.fabmax.kool.modules.ui2.PointerEvent
import de.fabmax.kool.modules.ui2.TextModifier
import de.fabmax.kool.modules.ui2.UiModifier
import de.fabmax.kool.modules.ui2.UiNode
import de.fabmax.kool.modules.ui2.UiRenderer
import de.fabmax.kool.modules.ui2.background
import de.fabmax.kool.modules.ui2.height
import de.fabmax.kool.modules.ui2.layout
import de.fabmax.kool.modules.ui2.text
import de.fabmax.kool.modules.ui2.width

@Immutable
interface Modifier {
    fun then(other: Modifier): Modifier = CombinedModifier(this, other)

    fun reduce(start: UiModifier)

    companion object : Modifier {
        override fun then(other: Modifier): Modifier = other

        override fun reduce(start: UiModifier) = Unit
    }
}

@Immutable
class CombinedModifier(val left: Modifier, val right: Modifier): Modifier {
    override fun reduce(start: UiModifier) {
        left.reduce(start)
        right.reduce(start)
    }
}

@Immutable
class BackgroundModifier(val background: UiRenderer<UiNode>): Modifier {
    override fun reduce(start: UiModifier) {
        start.background(background)
    }
}

@Immutable
class OnEnterModifier(val onEnter: (PointerEvent) -> Unit): Modifier {
    override fun reduce(start: UiModifier) {
        start.onEnter.add(onEnter)
    }
}
@Immutable
class OnExitModifier(val onEnter: (PointerEvent) -> Unit): Modifier {
    override fun reduce(start: UiModifier) {
        start.onExit.add(onEnter)
    }
}

@Immutable
class OnClickModifier(val onClick: (PointerEvent) -> Unit): Modifier {
    override fun reduce(start: UiModifier) {
        start.onClick.add(onClick)
    }
}

@Immutable
class SetTextModifier(val text: String): Modifier {
    override fun reduce(start: UiModifier) { (start as? TextModifier)?.text(text) }
}

class SizeModifier(val x: Dimension, val y: Dimension): Modifier {
    override fun reduce(start: UiModifier) {
        start.width(x)
        start.height(y)
    }
}

class RenderModifier(val render: UiNode.() -> Unit): Modifier {
    override fun reduce(start: UiModifier) {
        start.onRender.add(render)
    }
}

class LayoutModifier(val layout: Layout): Modifier {
    override fun reduce(start: UiModifier) {
        start.layout(layout)
    }
}


fun Modifier.background(background: UiRenderer<UiNode>) = then(BackgroundModifier(background))
fun Modifier.onEnter(run: (PointerEvent) -> Unit) = then(OnEnterModifier(run))
fun Modifier.onExit(run: (PointerEvent) -> Unit) = then(OnExitModifier(run))
fun Modifier.onClick(run: (PointerEvent) -> Unit) = then(OnClickModifier(run))
fun Modifier.text(text: String) = then(SetTextModifier(text))
fun Modifier.size(x: Dimension, y: Dimension = x) = then(SizeModifier(x, y))
fun Modifier.onRender(render: UiNode.() -> Unit) = then(RenderModifier(render))
fun Modifier.layout(layout: Layout) = then(LayoutModifier(layout))
