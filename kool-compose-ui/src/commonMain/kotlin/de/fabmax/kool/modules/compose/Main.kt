package de.fabmax.kool.modules.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.Composition
import androidx.compose.runtime.Recomposer
import de.fabmax.kool.modules.ksl.lang.KslExprBool1
import de.fabmax.kool.modules.ksl.lang.KslScopeBuilder
import kotlinx.coroutines.Dispatchers
import kotlin.reflect.KProperty

/**
 * Quick overview of compose compiler used for a sample DSL.
 *
 * Below this example are some notes on things compose enables.
 *
 * Resources:
 *  - Similar DSL idea with some explanation, https://arunkumar.dev/jetpack-compose-for-non-ui-tree-construction-and-code-generation/
 *  - [ExpressionNode] instructs compose to emit nodes to children via [ComposeNode]
 *  - [ShaderNodeApplier] handles adding children
 *  - [ExampleNodes] contains example definitions for [If] [For] etc...
 */
fun main() {
    // Simple node that contains an [expression] and mutable list of [children]
    val rootNode = ExampleShaderNode()
    // We never use the recomposer here, but it needs to be provided.
    val composition = Composition(ShaderNodeApplier(rootNode), Recomposer(Dispatchers.Main))
    composition.setContent {
        ExampleShader()
    }
    // Node provides an example render function
    println(rootNode.render())
}


/**
 * Outputs:
 * ```
 *   var test: String = "initial"
 *   var delegate: String = "initial"
 *   for (i in 0..10) {
 *     <Read delegate>
 *     if (i == '1') {
 *       test = "newValue"
 *       <example exec>
 *     }
 *   }
 * ```
 */
@Composable
fun ExampleShader() {
    val test = shaderVar("test", "initial")
    val delegateTest by shaderVar("delegate", "initial")

    For("0", "10") { i ->
        delegateTest
        If("$i == '1'") {
            test set "newValue"
            Exec("<example exec>")
        }
    }
}

// === Compose prevents errors when forgetting to pass scopes ===

/**
 * Example where KslScopeBuilder was forgotten on the lambda parameter,
 * this function will compile but add the inner [block] to the wrong scope.
 */
fun KslScopeBuilder.forgotScopeOnBlock(condition: KslExprBool1, block: /*KslScopeBuilder.*/() -> Unit) {
    `if`(condition) {
        block()
    }
}

/**
 * Similar example where @Composable was forgotten on the lambda parameter.
 *
 * Attempting to call another composable inside [block] will result in a compiler error.
 */
@Composable
fun ForgotScopeOnBlock(condition: String, block: /*@Composable*/ () -> Unit) {
    If(condition) {
        block()
    }
}

// === Compose allows delegate READs to be used to emit shader code ===

class ShaderVariable(val name: String, val type: String) {
    /**
     * Composable getValue lets us emit to the context a variable is read from.
     *
     * I think delegates in a DSL can normally do this, see example println inside
     * Not sure if the current dsl ever needs this but worth noting.
     */
    @Composable
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        println("thisRef on delegate '$name' was $thisRef") // Will be null inside DSL scope
        Exec("<Read $name>") // finds correct caller scope
        return ""
    }

    /**
     * Composable setValue is not supported (yet?) :(
     *
     * It's hard to find info on why this is a limitation but would avoid the current
     * `set` syntax if support is ever added.
     */
//    @Composable
//    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
//        Exec("Set name")
//        return ""
//    }

    /** Same syntax as current dsl */
    @Composable
    infix fun set(value: String) {
        Exec("$name = \"$value\"")
    }
}

@Composable
fun shaderVar(name: String, init: String): ShaderVariable {
    Exec("var $name: String = \"$init\"")
    return ShaderVariable(name, "String")
}

