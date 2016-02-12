package org.dungeongardener.util.genlang.nodes

import org.dungeongardener.util.Context

/**
 * Function node for infix operations
 */
class InfixExpr(val op: String,
                val left: Expression,
                val right: Expression,
                val func: (Any?, Any?) -> Any?) : Expression {

    override fun <T> evaluate(context: Context): T {
        val l = left.evaluate<Any?>(context)
        val r = right.evaluate<Any?>(context)
        return func(l, r) as T
    }

    override fun toString(): String {
        return "$left $op $right"
    }
}