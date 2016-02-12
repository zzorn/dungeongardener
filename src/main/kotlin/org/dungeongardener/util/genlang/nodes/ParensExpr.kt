package org.dungeongardener.util.genlang.nodes

import org.dungeongardener.util.Context

/**
 *
 */
class ParensExpr(val node: Expression) : Expression {
    override fun <T> evaluate(context: Context): T {
        return node.evaluate<T>(context)
    }

    override fun toString(): String {
        return "($node)"
    }
}