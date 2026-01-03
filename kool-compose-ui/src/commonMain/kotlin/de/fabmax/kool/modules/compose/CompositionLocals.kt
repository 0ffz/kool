package de.fabmax.kool.modules.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import de.fabmax.kool.modules.ui2.Colors
import de.fabmax.kool.modules.ui2.Sizes
import de.fabmax.kool.modules.ui2.UiSurface

val LocalUiSurface = compositionLocalOf<UiSurface> { error("No UiSurface provided") }

val LocalColors = compositionLocalOf<Colors> { error("No Colors provided") }

val LocalSizes = compositionLocalOf<Sizes> { error("No Sizes provided") }

val Colors @Composable get() = LocalColors.current
val Sizes @Composable get() = LocalSizes.current
