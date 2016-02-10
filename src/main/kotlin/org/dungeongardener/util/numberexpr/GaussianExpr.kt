package org.dungeongardener.util.numberexpr

import org.dungeongardener.util.Context

/**
 *
 */
data class GaussianExpr(val mean: NumExpr, val standardDeviation: NumExpr): NumExpr {
    override fun evaluate(context: Context): Double {
        return context.random.nextGaussian(
                mean.evaluate(context),
                standardDeviation.evaluate(context))
    }

    override fun toString(): String {
        return "gaussian($mean, $standardDeviation)"
    }
}