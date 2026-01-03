package de.fabmax.kool.modules.compose.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import de.fabmax.kool.modules.ui2.MutableStateValue

/**
 * Collects a Kool [MutableStateValue] as a compose [State].
 */
@Composable
fun <T> MutableStateValue<T>.collectAsState(): State<T> {
    return produceState(this.value) {
        onChange { old, new -> this.value = new }
    }
}
