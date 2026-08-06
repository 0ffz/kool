package de.fabmax.kool.modules.compose.composables

interface ContentDrawScope : DrawScope {
    /** Causes child drawing operations to run during the `onPaint` lambda. */
    fun drawContent()
}