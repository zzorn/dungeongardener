package org.dungeongardener.namegen.generator.nodes

import org.dungeongardener.namegen.GeneratorContext
import org.dungeongardener.namegen.generator.ContentBuilder
import org.flowutils.random.RandomSequence

/**
 *
 */
data class ReferenceNode<T, R>(val generatorId: String): GeneratorNode<T, R> {

    override fun generate(random: RandomSequence, context: GeneratorContext<T, R>, builder: ContentBuilder<T, R>) {
        context.generate(generatorId, random, builder)
    }
}