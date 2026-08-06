package de.fabmax.kool.modules.compose.composables

import androidx.compose.ui.unit.Density
import de.fabmax.kool.math.Vec4f
import de.fabmax.kool.modules.compose.node.ComposeUiSurface

interface DrawScope : Density {
    val x: Float
    val y: Float
    val width: Float
    val height: Float
    val surface: ComposeUiSurface

    val clipBounds get() = Vec4f(x, y, x + width, y + height)
}