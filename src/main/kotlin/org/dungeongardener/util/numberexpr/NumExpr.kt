package org.dungeongardener.util.numberexpr

import org.dungeongardener.util.Context

/**
 * Represents a number calculated using some expression.
 */
interface NumExpr {

    fun evaluate(context: Context): Double

}