package org.dungeongardener.util.randomnumber

/**
 * Represents a number calculated using some expression.
 */
interface NumExpr {

    fun evaluate(context: NumContext): Double

}