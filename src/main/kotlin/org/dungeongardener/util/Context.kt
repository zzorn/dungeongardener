package org.dungeongardener.util

import org.flowutils.Symbol
import org.flowutils.random.RandomSequence

/**
 * Generic context
 */
interface Context {

    val random: RandomSequence

    /**
     * @return the referenced item of the specified type, or null if not found
     */
    fun <T>getReferenceOrNull(ref: Symbol): T?

    /**
     * @return the referenced item of the specified type, or null if not found
     */
    fun <T>getReferenceOrNull(ref: String): T? = getReferenceOrNull<T>(Symbol.get(ref))

    /**
     * @return the referenced item of the specified type.  Throws exception if not found.
     */
    fun <T>getReference(ref: Symbol): T = getReferenceOrNull<T>(ref) ?: throw IllegalArgumentException("No reference named '$ref' found")

    /**
     * @return the referenced item of the specified type.  Throws exception if not found.
     */
    fun <T>getReference(ref: String): T = getReference<T>(Symbol.get(ref))

    fun setReference(name: String, value: Any)

}