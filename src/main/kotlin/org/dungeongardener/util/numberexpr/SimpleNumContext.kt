package org.dungeongardener.util.numberexpr

import org.flowutils.Symbol
import org.flowutils.random.RandomSequence
import org.flowutils.random.XorShift
import java.util.*

/**
 * Simple NumContext with support for storing named constants.
 */
class SimpleNumContext(initialValues: Map<String, Double>? = null,
                       override val random: RandomSequence = XorShift()) : NumContext {

    private val constants: MutableMap<Symbol, ConstantExpr> = HashMap()

    init {
        if (initialValues != null) set(initialValues)
    }

    fun set(values: Map<String, Double>) {
        for (entry in values) {
            set(entry.key, entry.value)
        }
    }

    operator fun set(name: String, value: Double) {
        val symbol = Symbol.get(name)
        val const = ConstantExpr(value)
        constants.put(symbol, const)
    }

    override fun get(expressionId: Symbol): NumExpr {
        val value = constants.get(expressionId)
        if (value != null) return value
        else throw IllegalArgumentException("No constant named '$expressionId' found in this context.")
    }
}