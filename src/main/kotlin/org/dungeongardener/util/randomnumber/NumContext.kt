package org.dungeongardener.util.randomnumber

import org.flowutils.Symbol
import org.flowutils.random.RandomSequence

/**
 *
 */
public interface NumContext {

    /**
     * Random value source.
     */
    val random: RandomSequence

    /**
     * Return a named expression
     */
    fun get(expressionId: Symbol): NumExpr

    fun get(expressionId: String): NumExpr = get(Symbol.get(expressionId))

    /**
     * Evaluate the specified expression
     */
    fun evaluate(expressionId: Symbol): Double {
        return get(expressionId).evaluate(this)
    }

    fun evaluate(expressionId: String): Double = evaluate(Symbol.get(expressionId))

}