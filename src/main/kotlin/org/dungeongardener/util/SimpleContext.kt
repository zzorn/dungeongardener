package org.dungeongardener.util

import org.flowutils.Symbol
import org.flowutils.random.RandomSequence
import org.flowutils.random.XorShift
import java.util.*

/**
 * A context backed by a map.
 * Can refer to a read-only parent context if one is provided.
 */
class SimpleContext(initialValues: Map<String, Any> = emptyMap(),
                    override val random: RandomSequence = XorShift(),
                    val parentContext: Context? = null): Context {

    constructor(parent: Context) : this(random = parent.random, parentContext = parent)

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

    override fun setReference(name: String, value: Any) {
        setReference(Symbol.get(name), value)
    }

    override fun <T> getReferenceOrNull(ref: Symbol): T? = references.get(ref) as T? ?: parentContext?.getReferenceOrNull<T>(ref)
}