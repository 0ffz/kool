package de.fabmax.kool.modules.compose.modifiers

import de.fabmax.kool.modules.ui2.UiModifier

fun interface ImmutableUiModifier : Modifier.Element {
    fun applyTo(uiModifier: UiModifier)
}

inline fun <reified T : UiModifier> Modifier.edit(crossinline edit: (T) -> Unit) = then(ImmutableUiModifier {
    if (it is T) edit(it)
})

inline fun <reified T : UiModifier> Modifier.optionalEdit(
    shouldApply: Boolean,
    crossinline edit: (T) -> Unit,
): Modifier {
    return if (shouldApply) edit(edit) else this
}
