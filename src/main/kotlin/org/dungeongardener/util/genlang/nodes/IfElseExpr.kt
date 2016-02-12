package org.dungeongardener.util.genlang.nodes

import org.dungeongardener.util.Context

/**
 *
 */
class IfElseExpr(val condition: Expression,
                 val ifValue: Expression,
                 val elseValue: Expression) : Expression {

    override fun <T> evaluate(context: Context): T {
        return if (condition.evaluate(context)) ifValue.evaluate<T>(context) else elseValue.evaluate(context)
    }

    override fun toString(): String {
        return "if ($condition) $ifValue else $elseValue"
    }
}