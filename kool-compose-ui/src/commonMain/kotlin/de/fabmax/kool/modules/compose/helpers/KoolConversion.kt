package de.fabmax.kool.modules.compose.helpers

import androidx.compose.ui.unit.dp

typealias KoolDp = de.fabmax.kool.modules.ui2.Dp
typealias ComposeDp = androidx.compose.ui.unit.Dp

fun KoolDp.toCompose(): ComposeDp = value.dp