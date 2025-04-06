package de.fabmax.kool.modules.compose.modifiers

import androidx.compose.runtime.Stable
import de.fabmax.kool.modules.ui2.*
import de.fabmax.kool.util.Color


@Stable
fun Modifier.background(background: UiRenderer<UiNode>) = edit<UiModifier> { it.background(background) }

@Stable
fun Modifier.backgroundColor(color: Color) = edit<UiModifier> { it.backgroundColor(color) }

fun Modifier.onEnter(run: (PointerEvent) -> Unit) = edit<UiModifier> { it.onEnter { run(it) } }

fun Modifier.onExit(run: (PointerEvent) -> Unit) = edit<UiModifier> { it.onExit { run(it) } }

fun Modifier.onClick(run: (PointerEvent) -> Unit) = edit<UiModifier> { it.onClick { run(it) } }

fun Modifier.draw(render: UiNode.() -> Unit) =
    edit<UiModifier> { it.onRender.add(render) }

@Stable
fun Modifier.text(text: String) = edit<TextModifier> { it.text(text) }

@Stable
fun Modifier.size(x: Dimension, y: Dimension = x) = edit<UiModifier> { it.size(x, y) }

@Stable
fun Modifier.width(width: Dimension) = edit<UiModifier> { it.width(width) }

@Stable
fun Modifier.height(height: Dimension) = edit<UiModifier> { it.height(height) }

@Stable
fun Modifier.layout(layout: Layout) = edit<UiModifier> { it.layout(layout) }

@Stable
fun Modifier.padding(start: Dp, end: Dp, top: Dp, bottom: Dp) = edit<UiModifier> {
    it.padding(start = start, end = end, top = top, bottom = bottom)
}

@Stable
fun Modifier.padding(horizontal: Dp, vertical: Dp = horizontal) = padding(horizontal, horizontal, vertical, vertical)

@Stable
fun Modifier.align(alignmentX: AlignmentX, alignmentY: AlignmentY) = edit<UiModifier> {
    it.align(alignmentX, alignmentY)
}

@Stable
fun Modifier.alignX(alignmentX: AlignmentX) = edit<UiModifier> {
    it.alignX(alignmentX)
}

@Stable
fun Modifier.alignY(alignmentY: AlignmentY) = edit<UiModifier> {
    it.alignY(alignmentY)
}

fun Modifier.dragListener(draggable: Draggable) = edit<UiModifier> { it.dragListener(draggable) }
