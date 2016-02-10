package org.dungeongardener.util.numberexpr

import org.dungeongardener.util.Context
import org.flowutils.Symbol

/**
 *
 */
data class ReferenceExpr(val referenceId: Symbol): NumExpr {
    override fun evaluate(context: Context): Double {

        val value = context.getReference<Any>(referenceId)

        return when (value) {
            is NumExpr -> value.evaluate(context)
            is Double -> value
            is Number -> value.toDouble()
            else -> throw IllegalArgumentException("Can not convert reference type '${value.javaClass.simpleName}' to a number")
        }
    }

    override fun toString(): String {
        return referenceId.toString()
    }
}