package org.dungeongardener.util.numberexpr

import org.dungeongardener.util.Context

/**
 *
 */
data class RandomExpr(val start: NumExpr, val end: NumExpr): NumExpr {
    override fun evaluate(context: Context): Double {
        return context.random.nextDouble(
                start.evaluate(context),
                end.evaluate(context))
    }

    override fun toString(): String {
        return "random($start, $end)"
    }

}