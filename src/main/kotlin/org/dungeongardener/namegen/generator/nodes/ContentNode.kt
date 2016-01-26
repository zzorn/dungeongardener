package org.dungeongardener.namegen.generator.nodes

import org.dungeongardener.namegen.GeneratorContext
import org.dungeongardener.namegen.generator.ContentBuilder
import org.flowutils.random.RandomSequence

/**
 *
 */
data class ContentNode<T, R>(val content: T) : GeneratorNode<T, R> {
    override fun generate(random: RandomSequence, context: GeneratorContext<T, R>, builder: ContentBuilder<T, R>) {
        builder.add(content)
    }
}