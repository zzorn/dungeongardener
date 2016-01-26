package org.dungeongardener.util.randomnumber

/**
 *
 */
data class RandomExpr(val start: NumExpr, val end: NumExpr): NumExpr {
    override fun evaluate(context: NumContext): Double {
        return context.random.nextDouble(
                start.evaluate(context),
                end.evaluate(context))
    }

    override fun toString(): String {
        return "random($start, $end)"
    }

}