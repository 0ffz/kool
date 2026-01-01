package de.fabmax.kool.modules.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode

@Composable
fun ExpressionNode(text: String, content: @Composable () -> Unit) {
    println("START: $text")
    ComposeNode<ExampleShaderNode, ShaderNodeApplier>(
        factory = {
            println("FACTORY: $text")
            ExampleShaderNode()
                  },
        update = {
            println("UPDATE: $text")

            set(text) {
                expression = it
            }
        },
        content = content,
    )
    println("END: $text")
}
