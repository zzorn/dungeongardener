package org.dungeongardener.generator

import org.flowutils.random.RandomSequence

/**
 * Generates some kind of possibly randomized instances of the specified type.
 */
interface Generator<T> {

    fun generate(parameters: Map<String, Any>? = null, random: RandomSequence? = null): T

}