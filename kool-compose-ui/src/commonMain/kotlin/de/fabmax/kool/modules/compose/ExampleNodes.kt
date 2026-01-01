package de.fabmax.kool.modules.compose

import androidx.compose.runtime.Composable

@Composable
fun If(condition: String, block: @Composable () -> Unit) {
    ExpressionNode("if ($condition)") {
        block()
    }
}

@Composable
fun For(start: String, end: String, block: @Composable (expr: String) -> Unit) {
    ExpressionNode("for (i in $start..$end)") {
        block("i")
    }
}

@Composable
fun Exec(text: String) = ExpressionNode(text) {}
