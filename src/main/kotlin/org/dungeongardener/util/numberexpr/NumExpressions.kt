package org.dungeongardener.util.numberexpr

import org.flowutils.Symbol
import org.flowutils.random.RandomSequence
import org.flowutils.random.XorShift
import java.util.*

/**
 *
 */
class NumExpressions(val expressions: MutableMap<Symbol, NumExpr> = LinkedHashMap()): NumContext {

//    private val parser = NumExprParser()

    override val random: RandomSequence = XorShift()

    fun addExpression(expressionId: Symbol, expression: NumExpr) {
        expressions.put(expressionId, expression)
    }

    fun addExpression(expressionId: Symbol, expressionText: String) {
//        addExpression(expressionId, parser.parseExpr(expressionText))
    }

    override fun get(expressionId: Symbol): NumExpr {
        return expressions.get(expressionId) ?: throw IllegalArgumentException("Could not find an expression named '${expressionId}'")
    }
}