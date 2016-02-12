package org.dungeongardener.util.genlang.nodes

import org.dungeongardener.util.Context

/**
 * A node for unary operators
 */
class UnaryExpr<N>(val operator: String, val expr: Expression, val func: (N) -> N) : Expression {

    override fun <T> evaluate(context: Context): T {
        return func(expr.evaluate<N>(context)) as T
    }

    override fun toString(): String {
        val separator = if (operator.last().isJavaIdentifierPart()) " " else ""
        return operator + separator + expr
    }
}