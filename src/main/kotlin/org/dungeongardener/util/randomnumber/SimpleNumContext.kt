package org.dungeongardener.util.randomnumber

import org.flowutils.Symbol
import org.flowutils.random.RandomSequence
import org.flowutils.random.XorShift

/**
 * Simple NumContext without support for named expressions.
 */
class SimpleNumContext(override val random: RandomSequence = XorShift()) : NumContext{

    override fun get(expressionId: Symbol): NumExpr {
        throw UnsupportedOperationException("No named expressions in this context")
    }
}