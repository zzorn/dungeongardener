package org.dungeongardener.namegen.generator.nodes

import org.dungeongardener.namegen.GeneratorContext
import org.dungeongardener.namegen.generator.ContentBuilder
import org.flowutils.random.RandomSequence

/**
 * Generates something by adding content objects to a builder.
 */
interface GeneratorNode<T, R> {

    fun generate(random: RandomSequence, context: GeneratorContext<T, R>, builder: ContentBuilder<T, R>)

    fun generate(random: RandomSequence, context: GeneratorContext<T, R>): R {
        val builder = context.createContentBuilder()
        generate(random, context, builder)
        return builder.build()
    }
}