package de.fabmax.kool.modules.compose

class ExampleShaderNode(
) {
    var parent: ExampleShaderNode? = null
    var expression: String = ""
    val children: MutableList<ExampleShaderNode> = mutableListOf()

    fun render(): String = buildString {
        if (expression.isNotEmpty()) append(expression)
        if(children.isNotEmpty()) {
            if(parent != null) append(" {")
            children.forEach {
                appendLine()
                append(it.render().prependIndent("  "))
            }
            appendLine()
            if(parent != null) append("}")
        }
    }
}
