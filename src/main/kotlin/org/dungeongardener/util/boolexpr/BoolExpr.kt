package org.dungeongardener.util.boolexpr

import org.dungeongardener.util.Context

/**
 * Boolean expression
 */
interface BoolExpr {

    fun evaluate(context: Context): Boolean

}