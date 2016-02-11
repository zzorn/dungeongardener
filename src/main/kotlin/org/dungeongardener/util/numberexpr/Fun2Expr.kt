package org.dungeongardener.util.numberexpr

import org.dungeongardener.util.Context

/**
 * Numerical function with two parameters.
 */
class Fun2Expr(name: String, p1: NumExpr, p2: NumExpr, val calc: (Double, Double) -> Double) : FunExpr(name, p1, p2) {
    override fun calculate(context: Context, paramValues: List<Double>): Double {
        return calc(paramValues[0], paramValues[1])
    }
}