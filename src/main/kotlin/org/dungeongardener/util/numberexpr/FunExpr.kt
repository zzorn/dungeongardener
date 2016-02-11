package org.dungeongardener.util.numberexpr

import org.dungeongardener.util.Context

/**
 * Base expression for functions with N parameters.
 */
abstract class FunExpr(val name: String, vararg val params: NumExpr) : NumExpr {

    protected abstract fun calculate(context: Context, paramValues: List<Double>): Double

    val parameterCount: Int = params.size

    override fun evaluate(context: Context): Double {
        return calculate(context, params.map {it.evaluate(context)})
    }

    override fun toString(): String {
        return name + params.joinToString(", ", "(", ")")
    }
}