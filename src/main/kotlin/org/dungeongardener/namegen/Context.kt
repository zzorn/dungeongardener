package org.dungeongardener.namegen

import org.flowutils.random.RandomSequence

/**
 * Generic context
 */
interface Context {

    val random: RandomSequence

    /**
     * @return the referenced item of the specified type, or null if not found
     */
    fun <T>getReference(ref: String, type: Class<T>): T?

}