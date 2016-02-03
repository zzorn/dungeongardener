package org.dungeongardener.util.numberexpr

import org.flowutils.Symbol

/**
 *
 */
data class ReferenceExpr(val referenceId: Symbol): NumExpr {
    override fun evaluate(context: NumContext): Double {
        return context.get(referenceId).evaluate(context)
    }

    override fun toString(): String {
        return referenceId.toString()
    }
}