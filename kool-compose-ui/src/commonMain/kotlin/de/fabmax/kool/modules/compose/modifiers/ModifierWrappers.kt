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

fun Modifier.onPositioned(run: (UiNode) -> Unit) = edit<UiModifier> { it.onPositioned { run(it) } }
fun Modifier.onMeasured(run: (UiNode) -> Unit) = edit<UiModifier> { it.onMeasured { run(it) } }
fun Modifier.draw(render: UiNode.() -> Unit) =
    edit<UiModifier> { it.onRender.add(render) }

@Stable
fun Modifier.size(size: Dimension) = size(size, size)

@Stable
fun Modifier.size(x: Dimension, y: Dimension) = edit<UiModifier> { it.size(x, y) }

fun Modifier.fillMaxWidth(weight: Float = 1f) = width(Grow(weight))
fun Modifier.fillMaxHeight(weight: Float = 1f) = height(Grow(weight))
fun Modifier.fillMaxSize(weight: Float = 1f) = size(Grow(weight))

@Stable
fun Modifier.width(width: Dimension) = edit<UiModifier> { it.width(width) }

@Stable
fun Modifier.height(height: Dimension) = edit<UiModifier> { it.height(height) }

@Stable
fun Modifier.layout(layout: Layout) = edit<UiModifier> { it.layout(layout) }

@Stable
fun Modifier.padding(all: Dp) = padding(all, all, all, all)

@Stable
fun Modifier.padding(start: Dp, end: Dp, top: Dp, bottom: Dp) = edit<UiModifier> {
    it.padding(start = start, end = end, top = top, bottom = bottom)
}

@Stable
fun Modifier.padding(horizontal: Dp, vertical: Dp = horizontal) = padding(horizontal, horizontal, vertical, vertical)

@Stable
fun Modifier.margin(all: Dp) = margin(all, all, all, all)

@Stable
fun Modifier.margin(horizontal: Dp, vertical: Dp) =
    margin(start = horizontal, end = horizontal, top = vertical, bottom = vertical)

@Stable
fun Modifier.margin(start: Dp, end: Dp, top: Dp, bottom: Dp) = edit<UiModifier> {
    it.margin(start = start, end = end, top = top, bottom = bottom)
}

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

