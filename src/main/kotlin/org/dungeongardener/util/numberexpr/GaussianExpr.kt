package org.dungeongardener.util.numberexpr

/**
 *
 */
data class GaussianExpr(val mean: NumExpr, val standardDeviation: NumExpr): NumExpr {
    override fun evaluate(context: NumContext): Double {
        return context.random.nextGaussian(
                mean.evaluate(context),
                standardDeviation.evaluate(context))
    }

    override fun toString(): String {
        return "gaussian($mean, $standardDeviation)"
    }
}