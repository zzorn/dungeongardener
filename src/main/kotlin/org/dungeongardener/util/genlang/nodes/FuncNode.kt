package org.dungeongardener.util.genlang.nodes

import org.dungeongardener.util.Context
import org.dungeongardener.util.parser.FunctionContext
import org.flowutils.Check

/**
 * Function call node
 */
class FunExpr(val name: String,
              val parameters: List<Expression>,
              val applyFun: (FunctionContext<Any>) -> Any) : Expression {

    init {
        Check.identifier(name, "name")
    }

    override fun <T> evaluate(context: Context): T {
        val funContext = FunctionContext(name, context, parameters.map {it.evaluate<Any>(context) })
        return applyFun(funContext) as T
    }

    override fun toString(): String {
        return "$name(${parameters.joinToString(", ")})"
    }
}


