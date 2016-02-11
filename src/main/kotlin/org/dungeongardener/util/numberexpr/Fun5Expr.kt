package org.dungeongardener.util.numberexpr

import org.dungeongardener.util.Context

/**
 * Numerical function with five parameters.
 */
class Fun5Expr(name: String, p1: NumExpr, p2: NumExpr, p3: NumExpr, p4: NumExpr, p5: NumExpr, val calc: (Double, Double, Double, Double, Double) -> Double) : FunExpr(name, p1, p2, p3, p4, p5) {
    override fun calculate(context: Context, paramValues: List<Double>): Double {
        return calc(paramValues[0], paramValues[1], paramValues[2], paramValues[3], paramValues[4])
    }
}