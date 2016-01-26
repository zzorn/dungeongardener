package org.dungeongardener.util.randomnumber

/**
 *
 */
data class ClampExpr(val expr: NumExpr, val minValue: NumExpr, val maxValue: NumExpr): NumExpr {
    override fun evaluate(context: NumContext): Double {
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