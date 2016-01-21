package org.dungeongardener.namegen

import org.dungeongardener.namegen.generator.ContentBuilder
import org.dungeongardener.namegen.generator.nodes.GeneratorNode
import org.dungeongardener.util.randomnumber.NumContext
import org.flowutils.random.RandomSequence
import org.flowutils.random.XorShift

/**
 *
 */
interface GeneratorContext<T, R> {

    /**
     * @return builder that can build result objects from content objects appropriate for this context.
     */
    fun createContentBuilder(): ContentBuilder<T, R>

    /**
     * Context used for evaluating various numerical values and constants
     */
    val numContext: NumContext

    /**
     * @return generator with the specified id, or null if no such generator found.
     */
    fun getGenerator(generatorId: String): GeneratorNode<T, R>?

    fun generate(generatorId: String, randomSeed: Long? = null): R {
        val builder = createContentBuilder()
        val random = if (randomSeed != null) XorShift(randomSeed) else XorShift()
        generate(generatorId, random, builder)
        return builder.build()
    }

    fun generate(generatorId: String, random: RandomSequence, builder: ContentBuilder<T, R>) {
        val generator = getGenerator(generatorId)
        if (generator != null) {
            generator.generate(random, this, builder)
        } else {
            throw IllegalArgumentException("Could not find generator '$generatorId'")
        }
    }
}