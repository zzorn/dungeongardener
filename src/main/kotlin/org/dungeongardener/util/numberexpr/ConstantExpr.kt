package org.dungeongardener.util.numberexpr

/**
 *
 */
data class ConstantExpr(val value: Double): NumExpr {
    override fun evaluate(context: NumContext): Double {
        return value
    }

    override fun toString(): String {
        return "$value"
    }
}