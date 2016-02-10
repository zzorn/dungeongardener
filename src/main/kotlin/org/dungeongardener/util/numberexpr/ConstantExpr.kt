package org.dungeongardener.util.numberexpr

import org.dungeongardener.util.Context

/**
 *
 */
data class ConstantExpr(val value: Double): NumExpr {
    override fun evaluate(context: Context): Double {
        return value
    }

    override fun toString(): String {
        return "$value"
    }
}