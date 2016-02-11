package org.dungeongardener.util.numberexpr

import org.dungeongardener.util.Context

/**
 * Numerical function with one parameter.
 */
class Fun1Expr(name: String, p1: NumExpr, val calc: (Double) -> Double) : FunExpr(name, p1) {
    override fun calculate(context: Context, paramValues: List<Double>): Double {
        return calc(paramValues[0])
    }
}