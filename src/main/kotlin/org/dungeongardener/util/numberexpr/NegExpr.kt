package org.dungeongardener.util.numberexpr

import org.dungeongardener.util.Context

/**
 *
 */
data class NegExpr(val e: NumExpr): NumExpr {
    override fun evaluate(context: Context): Double {
        return -e.evaluate(context)
    }

    override fun toString(): String {
        return "-$e"
    }
}