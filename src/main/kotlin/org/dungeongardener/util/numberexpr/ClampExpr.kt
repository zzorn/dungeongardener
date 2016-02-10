package org.dungeongardener.util.numberexpr

import org.dungeongardener.util.Context

/**
 *
 */
data class ClampExpr(val expr: NumExpr, val minValue: NumExpr, val maxValue: NumExpr): NumExpr {
    override fun evaluate(context: Context): Double {
        var value = expr.evaluate(context)
        val min = minValue.evaluate(context)
        val max = maxValue.evaluate(context)
        if (value < min) value = min
        if (value > max) value = max
        return value
    }

    override fun toString(): String {
        return "clamp($expr, $minValue, $maxValue)"
    }
}