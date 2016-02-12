package org.dungeongardener.util.genlang.nodes

import org.dungeongardener.util.Context
import org.flowutils.Symbol


/**
 *
 */
data class ReferenceExpr(val referenceId: Symbol): Expression {

    override fun <T> evaluate(context: Context): T {

        val value = context.getReference<Any>(referenceId)

        return when (value) {
            is Expression -> value.evaluate(context)
            else -> value as T
        }
    }

    override fun toString(): String {
        return referenceId.toString()
    }
}