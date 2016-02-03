package org.dungeongardener.util.numberexpr

/**
 * Represents a number calculated using some expression.
 */
interface NumExpr {

    fun evaluate(context: NumContext): Double

}