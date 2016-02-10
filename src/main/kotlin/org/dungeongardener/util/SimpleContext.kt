package org.dungeongardener.util

import org.flowutils.Symbol
import org.flowutils.random.RandomSequence
import org.flowutils.random.XorShift
import java.util.*

/**
 * A context backed by a map.
 */
class SimpleContext(initialValues: Map<String, Any> = emptyMap(),
                    override val random: RandomSequence = XorShift()): Context {

    var references: MutableMap<Symbol, Any> = HashMap()

    init {
        addValues(initialValues)
    }

    fun addValues(values: Map<String,Any>) {
        for (entry in values) {
            setReference(entry.key, entry.value)
        }
    }

    fun setReference(id: Symbol, value: Any) {
        references.put(id, value)
    }

    fun setReference(name: String, value: Any) {
        setReference(Symbol.get(name), value)
    }

    override fun <T> getReferenceOrNull(ref: Symbol): T? = references.get(ref) as T?
}